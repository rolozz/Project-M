package com.java.project.messageservice.repository;

import com.java.project.messageservice.model.ChatMessage;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface MessageRepository extends MongoRepository<ChatMessage, String> {

    List<ChatMessage> findByChatRoomId(String chatRoomId);
}
