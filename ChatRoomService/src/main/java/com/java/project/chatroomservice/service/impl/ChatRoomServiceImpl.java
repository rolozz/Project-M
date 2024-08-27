package com.java.project.chatroomservice.service.impl;

import com.java.project.chatroomservice.model.ChatRoom;
import com.java.project.chatroomservice.model.CreateChatRoomRequest;
import com.java.project.chatroomservice.repository.ChatRoomRepository;
import com.java.project.chatroomservice.service.ChatRoomService;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ChatRoomServiceImpl implements ChatRoomService {

    ChatRoomRepository chatRoomRepository;

    @Override
    public List<ChatRoom> findAll() {
        return chatRoomRepository.findAll();
    }

    @Override
    @Transactional
    public ChatRoom create(CreateChatRoomRequest createChatRoomRequest) {
        ChatRoom chatRoom = ChatRoom.builder()
                .name(createChatRoomRequest.getRoomName())
                .build();
        chatRoom.addParticipant(createChatRoomRequest.getCreatorUserName(), "ADMIN");
        return chatRoomRepository.save(chatRoom);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        chatRoomRepository.deleteById(id);
    }

    @Override
    public boolean existById(Long id) {
        return chatRoomRepository.existsById(id);
    }
}
