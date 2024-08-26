package com.java.project.messageservice.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.java.project.messageservice.dto.ChatMessageDto;
import com.java.project.messageservice.service.MessageService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class KafkaConsumerService {

    ObjectMapper objectMapper;
    MessageService messageService;
    KafkaProducerService kafkaProducerService;

    @KafkaListener(topics = "client-messages", groupId = "message-service-group")
    public void listen(String messagePayload) {
        try {
            ChatMessageDto chatMessage = objectMapper.readValue(messagePayload, ChatMessageDto.class);
            messageService.saveMessage(chatMessage);
            kafkaProducerService.sendMessage(chatMessage, "processed-messages");
        } catch (Exception e) {
            log.error("Error processing message: {}", e.getMessage());
        }
    }
}
