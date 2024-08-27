package com.java.project.websocketservice.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ChatMessageDto {

    public enum MessageType {
        CHAT, JOIN, LEAVE
    }

    private String chatRoomId;
    private String sender;
    private String content;
    private long timestamp;
    private MessageType type;
}
