package com.capstone.lms_service.messaging;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserListener {
    private static final Logger logger = LoggerFactory.getLogger(UserListener.class);
    @RabbitListener(queues = "versapath.user.created")
    public void handleMoodleUserCreation(String userEvent) {
        // TODO: create user to moodle
        logger.info("Start creating user: {}", userEvent);
    }
}
