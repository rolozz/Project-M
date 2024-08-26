package com.java.project.chatroomservice.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "chat_rooms")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ChatRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String name;

    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL)
    Set<ChatRoomParticipant> participants = new HashSet<>();

    public void addParticipant(String username, String role) {
        participants.add(ChatRoomParticipant.builder()
                .username(username)
                .role(role)
                .chatRoom(this)
                .build());
    }

    public void removeParticipant(String username) {
        participants.removeIf(participant -> participant.getUsername().equals(username));
    }
}
