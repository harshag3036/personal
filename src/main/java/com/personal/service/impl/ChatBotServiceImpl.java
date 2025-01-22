package com.personal.service.impl;

import com.personal.model.db.ChatDataEntity;
import com.personal.model.request.AddChatBotDataRequest;
import com.personal.repository.ChatDataRepository;
import com.personal.service.ChatBotService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class ChatBotServiceImpl implements ChatBotService {

    @Autowired ChatDataRepository chatDataRepository;
    @Override
    public List<String> getResponseFromWords(String message) {
        if(ObjectUtils.isNotEmpty(message)){
            log.info("ChatBot response called for - {}",message);
            return chatDataRepository.getResponseFromWords(message);
        }
        return null;
    }

    @Override
    public String addChatBotData(AddChatBotDataRequest addChatBotDataRequest) {
        if(ObjectUtils.isNotEmpty(addChatBotDataRequest) && ObjectUtils.isNotEmpty(addChatBotDataRequest.getChatDataWithResponse())){
            List<ChatDataEntity> chatDataEntityList = new ArrayList<>();
            addChatBotDataRequest.getChatDataWithResponse().forEach((key,value)->{
                ChatDataEntity chatDataEntity = new ChatDataEntity();
                chatDataEntity.setKeywords(key);
                chatDataEntity.setResponse(value);
                chatDataEntityList.add(chatDataEntity);
            });
            chatDataRepository.saveAll(chatDataEntityList);
            return "Chat Data added successfully";
        } else {
            return "Chat Data not added";
        }
    }
}
