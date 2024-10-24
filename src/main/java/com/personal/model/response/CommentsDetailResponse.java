package com.personal.model.response;

import lombok.*;

import java.util.UUID;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentsDetailResponse {
    private UUID id;
    private UUID postId;
    private String content;
    private String author;
    private Integer likes;
}
