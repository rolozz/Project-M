package com.java.project.websocketservice.config;

import com.java.project.websocketservice.util.handler.ChatWebSocketHandler;
import com.java.project.websocketservice.util.interceptor.RoomIdValidationIntercepter;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;


@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    ChatWebSocketHandler chatWebSocketHandler;
    RoomIdValidationIntercepter roomIdValidationIntercepter;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(chatWebSocketHandler, "/ws/chat")
                .addInterceptors(roomIdValidationIntercepter)
                .setAllowedOrigins("*");
    }
}
