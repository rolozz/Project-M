package com.java.project.messageservice.service;

import com.java.project.messageservice.dto.ChatMessageDto;

public interface KafkaProducerService {

    void sendMessage(ChatMessageDto message, String topic);

}
