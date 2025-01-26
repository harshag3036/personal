package com.personal.service.impl;

import com.personal.mapper.EntityToRequestMapper;
import com.personal.model.db.ChatDataEntity;
import com.personal.model.db.FeedbackEntity;
import com.personal.model.request.AddChatBotDataRequest;
import com.personal.model.response.ChatDataObject;
import com.personal.repository.ChatDataRepository;
import com.personal.repository.FeedbackRepository;
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
    @Autowired
    FeedbackRepository feedbackRepository;

    @Autowired
    EntityToRequestMapper entityToRequestMapper;
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

    @Override
    public List<ChatDataObject> getChatDataFromKeyword(String keyword) {

        List<ChatDataObject> chatDataObjectList = new ArrayList<>();
        if(ObjectUtils.isEmpty(keyword)){
            return chatDataObjectList;
        }
        List<ChatDataEntity> chatDataEntityList = chatDataRepository.getChatEntityFromKeywords(keyword);

        chatDataEntityList.forEach(chatDataEntity -> {
            ChatDataObject chatDataObject = entityToRequestMapper.mapChatDataEntityToChatDataObject(chatDataEntity);
            chatDataObjectList.add(chatDataObject);
        });

        return chatDataObjectList;
    }

    @Override
    public String updateChatData(ChatDataObject chatDataObject) {
        if(ObjectUtils.isNotEmpty(chatDataObject)){
            ChatDataEntity chatDataEntity = entityToRequestMapper.mapChatDataObjectToChatDataEntity(chatDataObject);
            chatDataRepository.save(chatDataEntity);
            return "Chat Data updated successfully";
        } else {
            return "Chat Data not updated";
        }
    }

    @Override
    public String deleteChatData(ChatDataObject chatDataObject) {
        if(ObjectUtils.isNotEmpty(chatDataObject)){
            ChatDataEntity chatDataEntity = entityToRequestMapper.mapChatDataObjectToChatDataEntity(chatDataObject);
            chatDataRepository.delete(chatDataEntity);
            return "Chat Data deleted successfully";
        } else {
            return "Chat Data not deleted";
        }
    }

    @Override
    public String saveFeedback(String feedback) {
        if (ObjectUtils.isNotEmpty(feedback)) {
            FeedbackEntity feedbackEntity = FeedbackEntity.builder()
                    .feedback(feedback).build();
            feedbackRepository.save(feedbackEntity);
            return "Feedback saved successfully";
        } else {
            return "Feedback not saved";
        }
    }
}
