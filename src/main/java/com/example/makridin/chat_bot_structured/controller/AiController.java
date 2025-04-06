package com.example.makridin.chat_bot_structured.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.converter.ListOutputConverter;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class AiController {
    private final ChatClient chatClient;

    public AiController(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    @GetMapping("/chat")
    public String getResponse(@RequestParam(value = "message", required = false) String request) {
        if (StringUtils.hasText(request)) {
            return chatClient.prompt()
                    .user(request)
                    .call()
                    .chatResponse()
                    .getResult().getOutput().getText();
        } else {
            return "There is nothing to search! please add a request parameter with the name message to your request!";
        }
    }

    @GetMapping("/listchat")
    public List<String> getListResponse(@RequestParam(value = "message", required = false) String request) {
        if (StringUtils.hasText(request)) {
            return chatClient.prompt()
                    .user(request)
                    .call()
                    .entity(new ListOutputConverter(new DefaultConversionService()));
        } else {
            return List.of("There is nothing to search! please add a request parameter with the name message to your request!");
        }
    }

    @GetMapping("/mapchat")
    public Map<String, Object> getMapResponse(@RequestParam(value = "message", required = false) String request) {
        if (StringUtils.hasText(request)) {
            return chatClient.prompt()
                    .user(request)
                    .call()
                    .entity(new ParameterizedTypeReference<Map<String, Object>>(){});
        } else {
            return Map.of("Wrong request!", "There is nothing to search! please add a request parameter with the name message to your request!");
        }
    }
}
