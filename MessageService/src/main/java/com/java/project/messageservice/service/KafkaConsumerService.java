package com.java.project.messageservice.service;

public interface KafkaConsumerService {

    void listen(String messagePayload);

}
