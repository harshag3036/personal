package com.personal.model.request;

import lombok.*;

import java.util.List;
import java.util.Map;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddChatBotDataRequest {
    private Map<String,String> chatDataWithResponse;
}
