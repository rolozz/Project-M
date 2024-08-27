package com.java.project.messageservice.model;

import com.java.project.messageservice.dto.ChatMessageDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "messages")
public class ChatMessage {

    @Id
    private String id;
    private String chatRoomId;
    private String sender;
    private String content;
    private long timestamp;
    private ChatMessageDto.MessageType type;
}
