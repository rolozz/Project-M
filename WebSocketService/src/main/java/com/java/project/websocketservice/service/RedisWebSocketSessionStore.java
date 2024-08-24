package com.java.project.websocketservice.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RedisWebSocketSessionStore {

    RedisTemplate<String, String> redisTemplate;
    Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();
    private static final String ROOM_PREFIX = "chat-room:";

    public String getCountSessions() {
        return String.valueOf(sessions.size());
    }

    public void addSession(String chatRoomId, WebSocketSession session) {
        sessions.put(session.getId(), session);
        redisTemplate.opsForSet().add(ROOM_PREFIX + chatRoomId, session.getId());
    }

    public void removeSession(String chatRoomId, WebSocketSession session) {
        sessions.remove(session.getId());
        redisTemplate.opsForSet().remove(ROOM_PREFIX + chatRoomId, session.getId());
    }

    public WebSocketSession getSession(String sessionId) {
        return sessions.get(sessionId);
    }

    public Set<String> getSessionIdsByRoomId(String chatRoomId) {
        return redisTemplate.opsForSet().members(ROOM_PREFIX + chatRoomId);
    }
}
