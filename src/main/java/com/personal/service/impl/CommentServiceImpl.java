package com.personal.service.impl;

import com.personal.mapper.EntityToRequestMapper;
import com.personal.model.db.CommentsEntity;
import com.personal.model.db.LoginEntity;
import com.personal.model.db.PostEntity;
import com.personal.model.request.CreateCommentRequest;
import com.personal.model.response.CommentsDetailResponse;
import com.personal.repository.CommentsTableRepository;
import com.personal.repository.LoginTableRepository;
import com.personal.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    EntityToRequestMapper entityToRequestMapper;

    @Autowired
    CommentsTableRepository commentRepository;

    @Autowired
    LoginTableRepository loginTableRepository;
    @Override
    public CommentsDetailResponse createComment(CreateCommentRequest createCommentRequest) {
        final CommentsEntity commentsEntity = entityToRequestMapper.mapCreateCommentRequestToCommentEntity(createCommentRequest);
        commentsEntity.setLikes(0);
        final CommentsEntity savedEntity = commentRepository.save(commentsEntity);
        final String username = getUsername(savedEntity);
        return new CommentsDetailResponse(savedEntity.getCommentId(),savedEntity.getPostId(), savedEntity.getComment(), username, savedEntity.getLikes());

    }

    private String getUsername(CommentsEntity commentsEntity) {
        final Optional<LoginEntity> loginEntity =  loginTableRepository.findById(commentsEntity.getAuthor());
        final String username;
        if(loginEntity.isPresent()){
            username = loginEntity.get().getUsername();
        }else {
            username = "Unknown";
        }
        return username;
    }

    @Override
    public Page<CommentsDetailResponse> getAllCommentsByPostId(UUID postId, Pageable pageable) {
        Page<CommentsEntity> commentsEntities = commentRepository.findByPostId(postId, pageable);
        return commentsEntities.map(commentsEntity -> new CommentsDetailResponse(
                commentsEntity.getCommentId(),
                commentsEntity.getPostId(),
                commentsEntity.getComment(),
                getUsername(commentsEntity),
                commentsEntity.getLikes()
        ));
    }

    @Override
    public CommentsDetailResponse increaseLikes(UUID commentId) {
        Optional<CommentsEntity> optionalComment = commentRepository.findById(commentId);
        if (optionalComment.isPresent()) {
            CommentsEntity comment = optionalComment.get();
            comment.setLikes(comment.getLikes() + 1);
            CommentsEntity updatedComment = commentRepository.save(comment);
            return new CommentsDetailResponse(
                    updatedComment.getCommentId(),
                    updatedComment.getPostId(),
                    updatedComment.getComment(),
                    getUsername(updatedComment),
                    updatedComment.getLikes()
            );
        } else {
            throw new RuntimeException("Comment not found");
        }
    }

}
