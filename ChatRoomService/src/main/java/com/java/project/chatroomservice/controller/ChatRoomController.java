package com.java.project.chatroomservice.controller;

import com.java.project.chatroomservice.model.ChatRoom;
import com.java.project.chatroomservice.model.CreateChatRoomRequest;
import com.java.project.chatroomservice.service.ChatRoomService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/chatrooms")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ChatRoomController {

    ChatRoomService chatRoomService;

    @GetMapping
    public List<ChatRoom> getAllRooms() {
        return chatRoomService.findAll();
    }

    @PostMapping
    public ChatRoom createRoom(@RequestBody CreateChatRoomRequest createChatRoomRequest) {
        return chatRoomService.create(createChatRoomRequest);
    }

    @DeleteMapping("/{id}")
    public void deleteRoom(@PathVariable Long id) {
        chatRoomService.delete(id);
    }

    @GetMapping("/exists/{roomId}")
    public boolean roomExists(@PathVariable Long roomId) {
        return chatRoomService.existById(roomId);
    }
}
