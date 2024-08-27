package com.java.project.websocketservice.service;

import com.java.project.websocketservice.dto.ChatMessageDto;

public interface KafkaProducerService {

    void sendMessage(ChatMessageDto message);

}
