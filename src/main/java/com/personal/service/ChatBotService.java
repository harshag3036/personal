package com.personal.service;

import com.personal.model.request.AddChatBotDataRequest;
import com.personal.model.response.ChatDataObject;

import java.util.List;

public interface ChatBotService {
    List<String> getResponseFromWords(String message);

    String addChatBotData(AddChatBotDataRequest addChatBotDataRequest);

    List<ChatDataObject> getChatDataFromKeyword(String keyword);

    String updateChatData(ChatDataObject chatDataObject);

    String deleteChatData(ChatDataObject chatDataObject);

    String saveFeedback(String feedback);
}
