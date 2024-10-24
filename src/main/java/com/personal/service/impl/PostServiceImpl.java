package com.personal.service.impl;

import com.personal.mapper.EntityToRequestMapper;
import com.personal.model.db.LoginEntity;
import com.personal.model.db.PostEntity;
import com.personal.model.request.CreatePostRequest;
import com.personal.model.response.PostDetailResponse;
import com.personal.repository.LoginTableRepository;
import com.personal.repository.PostTableRepository;
import com.personal.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class PostServiceImpl implements PostService {

    @Autowired
    PostTableRepository postTableRepository;
    @Autowired
    LoginTableRepository loginTableRepository;

    @Autowired
    EntityToRequestMapper entityToRequestMapper;
    @Override
    public PostDetailResponse createPost(CreatePostRequest createPostRequest) {
        final PostEntity postEntity = entityToRequestMapper.mapCreatePostRequestToPostEntity(createPostRequest);
        postTableRepository.save(postEntity);
        final String username = getUsername(postEntity);
        return new PostDetailResponse(postEntity.getPostId(), postEntity.getTitle(), postEntity.getContent(), username);
    }

    private String getUsername(PostEntity postEntity) {
        final Optional<LoginEntity> loginEntity =  loginTableRepository.findById(postEntity.getCustomerId());
        final String username;
        if(loginEntity.isPresent()){
            username = loginEntity.get().getUsername();
        }else {
            username = "Unknown";
        }
        return username;
    }

    @Override
    public Page<PostDetailResponse> getAllPosts(Pageable pageable) {
        Page<PostEntity> postEntities = postTableRepository.findAll(pageable);
        return postEntities.map(postEntity -> new PostDetailResponse(
                postEntity.getPostId(),
                postEntity.getTitle(),
                postEntity.getContent(),
               getUsername(postEntity)
        ));
    }

    @Override
    public Page<PostDetailResponse> getAllPostsByCustomerId(UUID customerId, Pageable pageable){
        Page<PostEntity> postEntities = postTableRepository.findByCustomerId(customerId,pageable);
        if(postEntities.isEmpty()){
            return Page.empty();
        }
        return postEntities.map(postEntity -> new PostDetailResponse(
                postEntity.getPostId(),
                postEntity.getTitle(),
                postEntity.getContent(),
                getUsername(postEntity)
        ));
    }
}
