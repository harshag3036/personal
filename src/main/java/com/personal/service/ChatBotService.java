package com.personal.service;

import com.personal.model.request.AddChatBotDataRequest;

import java.util.List;

public interface ChatBotService {
    List<String> getResponseFromWords(String message);

    String addChatBotData(AddChatBotDataRequest addChatBotDataRequest);
}
