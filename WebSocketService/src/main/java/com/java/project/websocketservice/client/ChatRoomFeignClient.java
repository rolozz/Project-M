package com.java.project.websocketservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "chatroom-service", url = "${chatroom.service.url}")
public interface ChatRoomFeignClient {

    @GetMapping("/chatrooms/exists/{roomId}")
    boolean roomExists(@PathVariable("roomId") String roomId);
}
