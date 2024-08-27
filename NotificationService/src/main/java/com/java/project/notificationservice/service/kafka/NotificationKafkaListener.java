package com.java.project.notificationservice.service.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.java.project.notificationservice.model.Notification;
import com.java.project.notificationservice.service.NotificationService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class NotificationKafkaListener {

    ObjectMapper objectMapper;
    NotificationService notificationService;

    @KafkaListener(topics = "notification-topic", groupId = "notification-group")
    public void consume(String notificationJson) {
        try {
            Notification notification = objectMapper.readValue(notificationJson, Notification.class);
            notificationService.saveNotification(notification);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
