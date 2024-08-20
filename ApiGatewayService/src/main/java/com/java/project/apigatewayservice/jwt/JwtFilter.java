package com.java.project.apigatewayservice.jwt;

import com.java.project.apigatewayservice.utils.UrlFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@RefreshScope
@Component
public class JwtFilter implements GatewayFilter {

    private final JwtUtil jwtUtil;
    private final UrlFilter urlFilter;

    @Autowired
    public JwtFilter(JwtUtil jwtUtil, UrlFilter urlFilter) {
        this.jwtUtil = jwtUtil;
        this.urlFilter = urlFilter;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        if (urlFilter.isSecured.test(request)) {
            String requiredRole = exchange.getAttribute("requiredRole");
            if (authMissing(request)) {
                return onError(exchange, HttpStatus.UNAUTHORIZED);
            }
            final String authHeader = request.getHeaders().getOrEmpty("Authorization").get(0);
            if (!authHeader.startsWith("Bearer ")) {
                return onError(exchange, HttpStatus.UNAUTHORIZED);
            }
            final String token = authHeader.substring(7).trim();

            try {
                if (jwtUtil.isExpired(token)) {
                    return onError(exchange, HttpStatus.UNAUTHORIZED);
                }
                if (requiredRole != null) {
                    String userRole = jwtUtil.extractRole(token);

                    if (!requiredRole.equals(userRole)) {
                        return onError(exchange, HttpStatus.FORBIDDEN);
                    }
                }
            } catch (Exception e) {
                return onError(exchange, HttpStatus.UNAUTHORIZED);
            }
        }
        return chain.filter(exchange);
    }

    private Mono<Void> onError(ServerWebExchange exchange, HttpStatus httpStatus) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);
        return response.setComplete();
    }

    private boolean authMissing(ServerHttpRequest request) {
        return !request.getHeaders().containsKey("Authorization");
    }
}
