package com.java.project.chatroomservice.repository;

import com.java.project.chatroomservice.model.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
}
