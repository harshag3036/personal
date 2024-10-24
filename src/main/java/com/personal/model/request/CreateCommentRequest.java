package com.personal.model.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.UUID;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateCommentRequest {
    @NotBlank
    private String comment;
    @NotBlank
    private UUID author;
    @NotBlank
    private UUID postId;
}
