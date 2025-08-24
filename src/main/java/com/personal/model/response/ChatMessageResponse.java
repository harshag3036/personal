package com.personal.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ChatMessageResponse {
    private String response;
    private String followUpQuestion;
    private boolean hasFollowUp;
    
    public ChatMessageResponse(String response) {
        this.response = response;
        this.hasFollowUp = false;
    }
    
    public ChatMessageResponse(String response, String followUpQuestion) {
        this.response = response;
        this.followUpQuestion = followUpQuestion;
        this.hasFollowUp = followUpQuestion != null && !followUpQuestion.isEmpty();
    }
}
