package com.java.project.websocketservice.controller;

import com.java.project.websocketservice.service.RedisWebSocketSessionStore;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/websocket")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class WebSocketController {

    RedisWebSocketSessionStore sessionStore;

    @GetMapping("/status")
    public String getStatus() {
        return sessionStore.getCountSessions();
    }
}
