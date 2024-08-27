package com.java.project.messageservice.controller;

import com.java.project.messageservice.dto.ChatMessageDto;
import com.java.project.messageservice.service.MessageService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/messages")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MessageController {

    MessageService messageService;

    @GetMapping("/room/{chatRoomId}")
    public List<ChatMessageDto> getMessages(@PathVariable String chatRoomId) {
        return messageService.getMessagesByRoomId(chatRoomId);
    }
}
