package com.personal.service;

import com.personal.model.request.CreateCommentRequest;
import com.personal.model.response.CommentsDetailResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface CommentService {
    CommentsDetailResponse createComment(CreateCommentRequest createCommentRequest);
    Page<CommentsDetailResponse> getAllCommentsByPostId(UUID postId, Pageable pageable);
    CommentsDetailResponse increaseLikes(UUID commentId);
}
