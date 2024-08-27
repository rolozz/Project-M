package com.java.project.chatroomservice.service;

import com.java.project.chatroomservice.model.ChatRoom;
import com.java.project.chatroomservice.model.CreateChatRoomRequest;
import com.java.project.chatroomservice.repository.ChatRoomRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ChatRoomService {

    ChatRoomRepository chatRoomRepository;

    public List<ChatRoom> findAll() {
        return chatRoomRepository.findAll();
    }

    public ChatRoom create(CreateChatRoomRequest createChatRoomRequest) {
        ChatRoom chatRoom = ChatRoom.builder()
                .name(createChatRoomRequest.getRoomName())
                .build();
        chatRoom.addParticipant(createChatRoomRequest.getCreatorUserName(), "ADMIN");
        return chatRoomRepository.save(chatRoom);
    }

    public void delete(Long id) {
        chatRoomRepository.deleteById(id);
    }

    public boolean existById(Long id) {
        return chatRoomRepository.existsById(id);
    }
}
