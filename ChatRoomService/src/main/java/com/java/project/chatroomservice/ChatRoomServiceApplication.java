package com.java.project.chatroomservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class ChatRoomServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ChatRoomServiceApplication.class, args);
    }

}
