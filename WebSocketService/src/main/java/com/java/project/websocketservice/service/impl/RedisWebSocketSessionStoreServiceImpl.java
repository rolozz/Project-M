package com.java.project.websocketservice.service.impl;

import com.java.project.websocketservice.service.RedisWebSocketSessionStoreService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RedisWebSocketSessionStoreServiceImpl implements RedisWebSocketSessionStoreService {

    static final String ROOM_PREFIX = "chat-room:";

    RedisTemplate<String, String> redisTemplate;
    Map<String, WebSocketSession> localSessions = new ConcurrentHashMap<>();

    @Override
    public void addSession(String chatRoomId, WebSocketSession session) {
        localSessions.put(session.getId(), session);
        redisTemplate.opsForSet().add(ROOM_PREFIX + chatRoomId, session.getId());
    }

    @Override
    public void removeSession(String chatRoomId, WebSocketSession session) {
        localSessions.remove(session.getId());
        redisTemplate.opsForSet().remove(ROOM_PREFIX + chatRoomId, session.getId());
    }

    @Override
    public WebSocketSession getSession(String sessionId) {
        return localSessions.get(sessionId);
    }

    @Override
    public Set<String> getSessionIdsByRoomId(String chatRoomId) {
        return redisTemplate.opsForSet().members(ROOM_PREFIX + chatRoomId);
    }

    @Override
    public boolean hasSessionInRoom(String chatRoomId) {
        return !getLocalSessionIdsByRoomId(chatRoomId).isEmpty();
    }

    @Override
    public Set<String> getLocalSessionIdsByRoomId(String chatRoomId) {
        return localSessions.entrySet().stream()
                .filter(entry -> redisTemplate.opsForSet().isMember(ROOM_PREFIX + chatRoomId, entry.getKey()))
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());
    }

    @Override
    public String getCountSessions() {
        return String.valueOf(localSessions.size());
    }

}
