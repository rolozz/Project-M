package com.java.project.websocketservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class WebSocketServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebSocketServiceApplication.class, args);
    }

}
