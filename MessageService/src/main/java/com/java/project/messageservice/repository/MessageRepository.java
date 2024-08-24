package com.java.project.messageservice.repository;

import com.java.project.messageservice.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {

    List<Message> findAllByChatRoomId(String chatRoomId);
}
