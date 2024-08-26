package com.java.project.messageservice.dto;

import lombok.Data;

@Data
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
