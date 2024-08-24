package com.java.project.websocketservice.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.java.project.websocketservice.model.ChatMessageDto;
import com.java.project.websocketservice.service.KafkaProducerService;
import com.java.project.websocketservice.service.RedisWebSocketSessionStore;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ChatWebSocketHandler extends TextWebSocketHandler {

    final ObjectMapper objectMapper;
    final KafkaProducerService kafkaProducerService;
    final RedisWebSocketSessionStore redisWebSocketSessionStore;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String chatRoomId = getChatRoomIdFromSession(session);
        redisWebSocketSessionStore.addSession(chatRoomId, session);
        System.out.println("Connected: " + session.getId() + " to chat room: " + chatRoomId);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        ChatMessageDto chatMessage = objectMapper.readValue(payload, ChatMessageDto.class);
        kafkaProducerService.sendMessage(chatMessage);
        session.sendMessage(new TextMessage("Message sent to Kafka: " + chatMessage.getContent()));
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        String chatRoomId = getChatRoomIdFromSession(session);
        redisWebSocketSessionStore.removeSession(chatRoomId, session);
        System.out.println("Disconnected: " + session.getId() + " from chat room: " + chatRoomId);
    }

    private String getChatRoomIdFromSession(WebSocketSession session) {
        return session.getUri().getQuery().split("=")[1];
    }
}
