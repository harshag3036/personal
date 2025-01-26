package com.personal.controller;

import com.personal.model.request.AddChatBotDataRequest;
import com.personal.model.response.ChatDataObject;
import com.personal.service.ChatBotService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping(path = "${api.v1.prefix}")
@Slf4j
@CrossOrigin
public class ChatBotController {

    @Autowired
    ChatBotService chatBotService;
    @PostMapping("/chatBot/data")
    public String addChatBotData(@RequestBody AddChatBotDataRequest addChatBotDataRequest) {
        log.info("ChatBot data called");
        return chatBotService.addChatBotData(addChatBotDataRequest);
    }

    @PostMapping("/chatBot/response")
    public List<String> getResponseFromWords(@RequestBody String message) {
        log.info("ChatBot response called for - {}",message);
        return chatBotService.getResponseFromWords(message);
    }

    @PostMapping("/chatBot/searchKeywords")
    public List<ChatDataObject> getChatDataFromKeywordResponse(@RequestBody String keyword) {
        log.info("getChatDataFromKeywordResponse called for - {}",keyword);
        return chatBotService.getChatDataFromKeyword(keyword);
    }

    @PostMapping("/chatBot/updateKeyword")
    public String updateKeyword(@RequestBody ChatDataObject chatDataObject) {
        log.info("updateKeyword called for - {}",chatDataObject.getDataId());
        return chatBotService.updateChatData(chatDataObject);
    }

    @PostMapping("/chatBot/deleteKeyword")
    public String deleteKeyword(@RequestBody ChatDataObject chatDataObject) {
        log.info("deleteKeyword called for - {}",chatDataObject.getDataId());
        return chatBotService.deleteChatData(chatDataObject);
    }

    @PostMapping("/feedback")
    public String feedback(@RequestBody String feedback) {
        log.info("feedback called for - {}",feedback);
        return chatBotService.saveFeedback(feedback);
    }

}
