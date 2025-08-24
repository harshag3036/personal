package com.personal.model.request;

import lombok.Data;

@Data
public class ChatMessageRequest {
    private String message;
    private Boolean firstChat = false;
    private String sessionId;
}
