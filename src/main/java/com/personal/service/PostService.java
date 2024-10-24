package com.personal.service;

import com.personal.model.request.CreatePostRequest;
import com.personal.model.response.PostDetailResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface PostService {

    PostDetailResponse createPost(CreatePostRequest createPostRequest);
    Page<PostDetailResponse> getAllPosts(Pageable pageable);
    Page<PostDetailResponse> getAllPostsByCustomerId(UUID customerId, Pageable pageable);

}
