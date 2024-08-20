package com.java.project.apigatewayservice.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("fallback")
public class FallBackController {

    @RequestMapping("/user-info-service")
    public Mono<String> userInfoServiceFallback() {
        return Mono.just("UserInfoService is dead");
    }

    @RequestMapping("/auth-server")
    public Mono<String> authServerFallback() {
        return Mono.just("AuthServer is dead");
    }
}
