package com.java.project.chatroomservice.controller;

import com.java.project.chatroomservice.model.ChatRoom;
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
    public ChatRoom createRoom(@RequestBody ChatRoom chatRoom) {
        return chatRoomService.create(chatRoom);
    }

    @DeleteMapping("/{id}")
    public void deleteRoom(@PathVariable Long id) {
        chatRoomService.delete(id);
    }
}
