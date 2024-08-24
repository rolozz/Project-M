package com.java.project.websocketservice.config;

import com.java.project.websocketservice.model.ChatMessageDto;
import com.java.project.websocketservice.service.RedisWebSocketSessionStore;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import lombok.RequiredArgsConstructor;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Set;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RedisMessageSubscriber implements MessageListener {

    private final RedisWebSocketSessionStore redisWebSocketSessionStore;
    private final ObjectMapper objectMapper;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            String channel = new String(message.getChannel());
            String chatRoomId = channel.split(":")[1];
            String payload = message.toString();

            ChatMessageDto chatMessage = objectMapper.readValue(payload, ChatMessageDto.class);
            Set<String> sessionIds = redisWebSocketSessionStore.getSessionIdsByRoomId(chatRoomId);

            for (String sessionId : sessionIds) {
                WebSocketSession session = redisWebSocketSessionStore.getSession(sessionId);
                if (session != null && session.isOpen()) {
                    session.sendMessage(new TextMessage(objectMapper.writeValueAsString(chatMessage)));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
