package com.java.project.websocketservice.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.java.project.websocketservice.dto.ChatMessageDto;
import com.java.project.websocketservice.redis.RedisWebSocketSessionStore;
import com.java.project.websocketservice.service.ChatService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ChatWebSocketHandler extends TextWebSocketHandler {

    ObjectMapper objectMapper;
    RedisWebSocketSessionStore redisWebSocketSessionStore;
    ChatService chatService;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String chatRoomId = getChatRoomIdFromSession(session);
        redisWebSocketSessionStore.addSession(chatRoomId, session);
        ChatMessageDto joinMessage = ChatMessageDto.builder()
                .chatRoomId(chatRoomId)
                .sender("SYSTEM")
                .content("User joined the chat")
                .timestamp(System.currentTimeMillis())
                .type(ChatMessageDto.MessageType.JOIN)
                .build();
        chatService.processAndSendMessage(joinMessage);
        log.info("Connected: {} to chat room: {}", session.getId(), chatRoomId);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        ChatMessageDto chatMessage = objectMapper.readValue(payload, ChatMessageDto.class);
        String chatRoomId = getChatRoomIdFromSession(session);
        chatMessage.setChatRoomId(chatRoomId);
        chatMessage.setTimestamp(System.currentTimeMillis());

        if (chatMessage.getType() == ChatMessageDto.MessageType.CHAT) {
            chatService.processAndSendMessage(chatMessage);
        }
        session.sendMessage(new TextMessage("Message sent to Kafka: " + chatMessage.getContent()));
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        String chatRoomId = getChatRoomIdFromSession(session);
        redisWebSocketSessionStore.removeSession(chatRoomId, session);
        ChatMessageDto leaveMessage = ChatMessageDto.builder()
                .chatRoomId(chatRoomId)
                .sender("SYSTEM")
                .content("User left the chat")
                .timestamp(System.currentTimeMillis())
                .type(ChatMessageDto.MessageType.LEAVE)
                .build();
        chatService.processAndSendMessage(leaveMessage);
        log.info("Disconnected: {} from chat room: {}", session.getId(), chatRoomId);
    }

    private String getChatRoomIdFromSession(WebSocketSession session) {
        return session.getUri().getQuery().split("=")[1];
    }
}
