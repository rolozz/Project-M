package com.java.project.chatroomservice.service;

import com.java.project.chatroomservice.model.ChatRoom;
import com.java.project.chatroomservice.model.CreateChatRoomRequest;

import java.util.List;

public interface ChatRoomService {

    List<ChatRoom> findAll();

    ChatRoom create(CreateChatRoomRequest createChatRoomRequest);

    void delete(Long id);

    boolean existById(Long id);


}
