package com.java.project.websocketservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.java.project.websocketservice.model.ChatMessageDto;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class KafkaConsumerService {

    private final ObjectMapper objectMapper;
    private final RedisTemplate<String, String> redisTemplate;

    @KafkaListener(topics = "chat-messages", groupId = "chat-group")
    public void consume(String message) {
        try {
            ChatMessageDto chatMessage = objectMapper.readValue(message, ChatMessageDto.class);
            String chatRoomId = chatMessage.getChatRoomId();


            redisTemplate.convertAndSend("chat-room:" + chatRoomId, message);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
