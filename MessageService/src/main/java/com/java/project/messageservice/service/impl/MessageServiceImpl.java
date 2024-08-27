package com.java.project.messageservice.service.impl;

import com.java.project.messageservice.dto.ChatMessageDto;
import com.java.project.messageservice.mapper.ChatMessageMapper;
import com.java.project.messageservice.model.ChatMessage;
import com.java.project.messageservice.repository.MessageRepository;
import com.java.project.messageservice.service.MessageService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class MessageServiceImpl implements MessageService {

    MessageRepository messageRepository;
    ChatMessageMapper chatMessageMapper = ChatMessageMapper.INSTANCE;

    @Override
    @Transactional
    public void saveMessage(ChatMessageDto messageDto) {
        ChatMessage message = chatMessageMapper.toEntity(messageDto);
        messageRepository.save(message);
        log.info("Saved message: {}", message);
        log.info("Saving message for chatRoomId: {}", message.getChatRoomId());
    }

    @Override
    public List<ChatMessageDto> getMessagesByRoomId(String roomId) {
        return chatMessageMapper.toDto(messageRepository.findByChatRoomId(roomId));
    }
}
