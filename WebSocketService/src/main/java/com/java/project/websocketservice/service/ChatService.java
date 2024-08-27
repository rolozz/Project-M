package com.java.project.websocketservice.service;

import com.java.project.websocketservice.dto.ChatMessageDto;

public interface ChatService {

    void processAndSendMessage(ChatMessageDto chatMessage);

    void validateMessage(ChatMessageDto chatMessage);


}
