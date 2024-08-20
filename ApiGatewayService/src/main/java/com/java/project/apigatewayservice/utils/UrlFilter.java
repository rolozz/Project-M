package com.java.project.apigatewayservice.utils;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Predicate;

@Component
public class UrlFilter {

    public static final List<String> publicEndpoints = List.of(
            "/api/user/register",
            "/api/auth/login"
    );
    public Predicate<ServerHttpRequest> isSecured =
            request -> publicEndpoints.stream()
                    .noneMatch(uri -> request.getURI().getPath().contains(uri));
}
