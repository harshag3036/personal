package com.personal.controller;

import com.personal.model.request.AddChatBotDataRequest;
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
}
