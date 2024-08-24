package com.java.project.messageservice.service;

import com.java.project.messageservice.model.Message;
import com.java.project.messageservice.repository.MessageRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MessageService {

    MessageRepository messageRepository;

    public Message saveMessage(Message message) {
        return messageRepository.save(message);
    }

    public List<Message> getMessagesByChatRoom(String chatRoomId) {
        return messageRepository.findAllByChatRoomId(chatRoomId);
    }
}
