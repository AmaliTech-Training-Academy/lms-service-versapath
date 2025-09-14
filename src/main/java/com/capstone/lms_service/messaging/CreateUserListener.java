package com.capstone.lms_service.messaging;

import com.capstone.lms_service.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.common.event.ProduceUserEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreateUserListener {
    private static final Logger logger = LoggerFactory.getLogger(CreateUserListener.class);
    private final UserService userService;
   @RabbitListener(queues = "${USER_CREATE_QUEUE}")
    public void handleMoodleUserCreation(ProduceUserEvent userEvent) throws JsonProcessingException {

        logger.info("Start creating user: {}", userEvent);
        userService.createUser(userEvent);

    }
}
