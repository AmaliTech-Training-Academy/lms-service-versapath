package com.capstone.lms_service.messaging;

import lombok.RequiredArgsConstructor;
import org.common.event.ErrorEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreateUserErrorProducer {
    private final RabbitTemplate rabbitTemplate;
    private static final Logger logger = LoggerFactory.getLogger(CreateUserErrorProducer.class);
    @Value("${USER_CREATE_ERROR_QUEUE}")
    private String userCreateErrorQueue;

    public void sendCreateErrorUserCommand(ErrorEvent errorEvent) {
        logger.info("Send command to display error: {}", errorEvent);
        rabbitTemplate.convertAndSend(userCreateErrorQueue, errorEvent);
    }
}
