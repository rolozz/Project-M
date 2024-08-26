package com.java.project.websocketservice.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.java.project.websocketservice.dto.ChatMessageDto;
import com.java.project.websocketservice.redis.RedisWebSocketSessionStore;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.util.Set;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class KafkaConsumerService {

    ObjectMapper objectMapper;
    RedisWebSocketSessionStore redisWebSocketSessionStore;

    @KafkaListener(topics = "processed-messages", groupId = "websocket-group")
    public void listen(String messagePayload) {
        try {
            ChatMessageDto chatMessage = objectMapper.readValue(messagePayload, ChatMessageDto.class);
            String chatRoomId = chatMessage.getChatRoomId();

            Set<String> sessionIds = redisWebSocketSessionStore.getSessionIdsByRoomId(chatRoomId);

            for (String sessionId : sessionIds) {
                WebSocketSession session = redisWebSocketSessionStore.getSession(sessionId);
                if (session != null && session.isOpen()) {
                    session.sendMessage(new TextMessage(objectMapper.writeValueAsString(chatMessage)));
                }
            }
        } catch (Exception e) {
            log.error("Error processing message: {}", e.getMessage());
        }
    }
}
