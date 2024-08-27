package com.java.project.messageservice.service;

import com.java.project.messageservice.dto.ChatMessageDto;

import java.util.List;

public interface MessageService {

    void saveMessage(ChatMessageDto messageDto);

    List<ChatMessageDto> getMessagesByRoomId(String roomId);

}
