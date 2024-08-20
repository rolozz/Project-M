package com.java.project.apigatewayservice.configs;

import com.java.project.apigatewayservice.jwt.JwtFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    private final JwtFilter jwtFilter;

    @Autowired
    public GatewayConfig(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;

    }

    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("update", r -> r.path("/api/user/update/**")
                        .filters(f -> f.filter(jwtFilter)
                                .filter((exchange, chain) -> {
                                    exchange.getAttributes().put("requiredRole", "USER");
                                    return chain.filter(exchange);
                                })
                                .circuitBreaker(config -> config
                                        .setName("userServiceCircuitBreaker")
                                        .setFallbackUri("forward:/fallback/user-info-service")))
                        .uri("lb://UserInfoService"))
                .route("register", r -> r.path("/api/user/register")
                        .filters(f -> f
                                .circuitBreaker(config -> config
                                        .setName("userServiceCircuitBreaker")
                                        .setFallbackUri("forward:/fallback/user-info-service")))
                        .uri("lb://UserInfoService"))
                .route("login", r -> r.path("/api/auth/login")
                        .filters(f -> f
                                .circuitBreaker(config -> config
                                        .setName("authServiceCircuitBreaker")
                                        .setFallbackUri("forward:/fallback/auth-server")))
                        .uri("lb://AuthServer"))
                .build();
    }
}