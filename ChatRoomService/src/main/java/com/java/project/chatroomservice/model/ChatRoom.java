package com.java.project.chatroomservice.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
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

    @Builder.Default
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "chat_room_id")
    Set<ChatRoomParticipant> participants = new HashSet<>();

    public void addParticipant(String username, String role) {
        participants.add(ChatRoomParticipant.builder()
                .username(username)
                .role(role)
                .build());
    }

    public void removeParticipant(String username) {
        participants.removeIf(participant -> participant.getUsername().equals(username));
    }
}
