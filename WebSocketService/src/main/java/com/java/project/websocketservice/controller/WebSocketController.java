package com.java.project.websocketservice.controller;

import com.java.project.websocketservice.service.RedisWebSocketSessionStoreService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/websocket")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class WebSocketController {

    RedisWebSocketSessionStoreService sessionStore;

    @GetMapping("/sessions/count")
    public String getStatus() {
        return sessionStore.getCountSessions();
    }

    @GetMapping("/sessions/room/{id}")
    public String getSessionIdsByRoom(@PathVariable String id) {
        return sessionStore.getSessionIdsByRoomId(id).toString();
    }
}
