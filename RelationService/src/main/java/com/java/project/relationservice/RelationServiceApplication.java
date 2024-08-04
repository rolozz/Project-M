package com.java.project.relationservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class RelationServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(RelationServiceApplication.class, args);
	}

}
