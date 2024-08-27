package com.java.project.websocketservice.service;

import org.springframework.web.socket.WebSocketSession;

import java.util.Set;

public interface RedisWebSocketSessionStoreService {

    void addSession(String chatRoomId, WebSocketSession session);

    void removeSession(String chatRoomId, WebSocketSession session);

    WebSocketSession getSession(String sessionId);

    Set<String> getSessionIdsByRoomId(String chatRoomId);

    boolean hasSessionInRoom(String chatRoomId);

    Set<String> getLocalSessionIdsByRoomId(String chatRoomId);

    String getCountSessions();

}
