package com.personal.controller;

import com.personal.model.request.CreatePostRequest;
import com.personal.model.response.PostDetailResponse;
import com.personal.service.PostService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RestController
@RequestMapping(path = "${api.v1.prefix}")
@Slf4j
@CrossOrigin
public class PostController {

    @Autowired
    PostService postService;
    @PostMapping("/createPost")
    public PostDetailResponse createPost(@RequestBody CreatePostRequest createPostRequest) {
        log.info("Create Post called");
        return postService.createPost(createPostRequest);
    }


    @GetMapping("/getAllPosts")
    public Page<PostDetailResponse> getAllPosts(@RequestParam(defaultValue = "0") int page,
                                                @RequestParam(defaultValue = "10") int size) {
        log.info("Get All Posts called");
        Pageable pageable = PageRequest.of(page, size);
        return postService.getAllPosts(pageable);
    }

    @GetMapping("/getAllPosts/{customerId}")
    public Page<PostDetailResponse> getAllPostsForCustomerId(@RequestParam(defaultValue = "0") int page,
                                                @RequestParam(defaultValue = "10") int size,  @PathVariable("customerId") UUID customerId) {
        log.info("Get All Posts called");
        Pageable pageable = PageRequest.of(page, size);
        return postService.getAllPostsByCustomerId(customerId,pageable);
    }
}
