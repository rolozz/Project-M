package com.java.project.chatroomservice.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateChatRoomRequest {
    String roomName;
    String creatorUserName;
}
