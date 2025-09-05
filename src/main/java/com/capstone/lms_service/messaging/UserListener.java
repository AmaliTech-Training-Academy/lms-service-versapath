package com.capstone.lms_service.messaging;

import org.common.event.CreateUserEvent;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class UserListener {
    @RabbitListener(queues = "versapath.user.created")
    public void handleMoodleUserCreation(CreateUserEvent userEvent) {
        // TODO: create user to moodle
        System.out.println("Start creating user: " + userEvent.getEmail());
    }
}
