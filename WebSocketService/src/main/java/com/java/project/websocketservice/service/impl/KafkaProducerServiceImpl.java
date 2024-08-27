package com.java.project.websocketservice.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.java.project.websocketservice.dto.ChatMessageDto;
import com.java.project.websocketservice.service.KafkaProducerService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class KafkaProducerServiceImpl implements KafkaProducerService {

    KafkaTemplate<String, String> kafkaTemplate;
    ObjectMapper objectMapper;

    @Override
    public void sendMessage(ChatMessageDto message) {
        try {
            String jsonMessage = objectMapper.writeValueAsString(message);
            kafkaTemplate.send("client-messages", jsonMessage);
            log.info("Sent message: {}", jsonMessage);
        } catch (Exception e) {
            log.error("Failed to send message: {}", e.getMessage());
        }
    }
}
