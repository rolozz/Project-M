package com.java.project.gatewayservice.configs;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class Routes {

    @Bean
    public RouteLocator relationRouteLocator(final RouteLocatorBuilder builder) {

        return builder.routes()
                .route("relationServiceTest", r -> r.path("/test")
                        .and().method("GET")
                        .uri("lb://RelationService")
                ).build();

    }

}
