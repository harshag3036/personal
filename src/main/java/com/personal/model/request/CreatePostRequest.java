package com.personal.model.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.UUID;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreatePostRequest {
    @NotBlank
    private String title;
    @NotBlank
    private String content;
    @NotBlank
    private UUID customerId;
}
