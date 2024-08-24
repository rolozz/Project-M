package com.java.project.websocketservice.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ChatMessageDto {
    String chatRoomId;
    String sender;
    String content;
    long timestamp;
    MessageType type;

    public enum MessageType {
        CHAT,
        JOIN,
        LEAVE
    }
}
