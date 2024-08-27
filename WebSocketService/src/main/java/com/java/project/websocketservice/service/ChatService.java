package com.java.project.websocketservice.service;

import com.java.project.websocketservice.dto.ChatMessageDto;
import com.java.project.websocketservice.kafka.KafkaProducerService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ChatService {


    KafkaProducerService kafkaProducerService;

    public void processAndSendMessage(ChatMessageDto chatMessage) {
        validateMessage(chatMessage);
        chatMessage.setTimestamp(System.currentTimeMillis());
        log.info("Processing message from {} in room {}: {}",
                chatMessage.getSender(), chatMessage.getChatRoomId(), chatMessage.getContent());
        kafkaProducerService.sendMessage(chatMessage);
    }

    private void validateMessage(ChatMessageDto chatMessage) {
        if (chatMessage.getChatRoomId() == null || chatMessage.getChatRoomId().isEmpty()) {
            throw new IllegalArgumentException("Chat room ID cannot be null or empty");
        }

        if (chatMessage.getSender() == null || chatMessage.getSender().isEmpty()) {
            throw new IllegalArgumentException("Sender cannot be null or empty");
        }

        if (chatMessage.getContent() == null || chatMessage.getContent().isEmpty()) {
            throw new IllegalArgumentException("Content cannot be null or empty");
        }
    }
}
