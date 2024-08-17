package com.java.project.apigatewayservice.configs;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;

public class GatewayConfig {

    @Bean
    public RouteLocator UserInfoLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("register_route", r -> r.path("/api/user/register")
                        .and()
                        .method(HttpMethod.POST)
                        .uri("http://localhost:8083"))
                .route("update_route", r -> r.path("/api/user/update/")
                        .and()
                        .method(HttpMethod.POST)
                        .uri("lb://UserInfoService"))
                .route("login_route", r -> r.path("/api/auth/login")
                        .and()
                        .method(HttpMethod.POST)
                        .uri("lb://AuthServer"))
                .build();
    }
}

