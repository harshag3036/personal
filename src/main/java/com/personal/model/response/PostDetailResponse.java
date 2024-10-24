package com.personal.model.response;

import lombok.*;

import java.util.UUID;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostDetailResponse {

    private UUID postId;
    private String title;
    private String content;
    private String customerUserName;
}
