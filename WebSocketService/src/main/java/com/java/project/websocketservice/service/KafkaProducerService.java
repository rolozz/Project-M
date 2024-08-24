package com.java.project.websocketservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.java.project.websocketservice.model.ChatMessageDto;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class KafkaProducerService {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public void sendMessage(ChatMessageDto chatMessage) throws Exception {
        String message = objectMapper.writeValueAsString(chatMessage);
        kafkaTemplate.send("chat-messages", message);
    }
}
