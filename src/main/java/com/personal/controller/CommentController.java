package com.personal.controller;

import com.personal.model.db.CommentsEntity;
import com.personal.model.request.CreateCommentRequest;
import com.personal.model.response.CommentsDetailResponse;
import com.personal.service.CommentService;
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
public class CommentController {

    @Autowired
    private CommentService commentService;

    @PostMapping("/createComment")
    public CommentsDetailResponse createComment(@RequestBody CreateCommentRequest createCommentRequest) {
        log.info("Create Comment called");
        return commentService.createComment(createCommentRequest);
    }

    @GetMapping("/getAllComments/{postId}")
    public Page<CommentsDetailResponse> getAllCommentsByPostId(@PathVariable("postId") UUID postId,
                                                               @RequestParam(defaultValue = "0") int page,
                                                               @RequestParam(defaultValue = "10") int size) {
        log.info("Get All Comments by PostId called");
        Pageable pageable = PageRequest.of(page, size);
        return commentService.getAllCommentsByPostId(postId, pageable);
    }

    @PostMapping("/increaseLikes/{commentId}")
    public CommentsDetailResponse increaseLikes(@PathVariable("commentId") UUID commentId) {
        log.info("Increase Likes called");
        return commentService.increaseLikes(commentId);
    }

}
