package com.java.project.messageservice.service.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.java.project.messageservice.model.Message;
import com.java.project.messageservice.service.MessageService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MessageKafkaListener {

    ObjectMapper objectMapper;
    MessageService messageService;

    @KafkaListener(topics = "chat-messages", groupId = "chat-group")
    public void consume(String messageJson) {
        try {
            Message message = objectMapper.readValue(messageJson, Message.class);
            messageService.saveMessage(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
