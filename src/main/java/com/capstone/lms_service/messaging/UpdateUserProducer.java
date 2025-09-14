package com.capstone.lms_service.messaging;

import lombok.RequiredArgsConstructor;
import org.common.event.UpdateUserEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UpdateUserProducer {
    private final RabbitTemplate rabbitTemplate;
    private static final Logger logger = LoggerFactory.getLogger(UpdateUserProducer.class);

    @Value("${USER_UPDATE_QUEUE}")
    private String userUpdateQueue;

    public void sendUpdateUserMoodleIdCommand(UpdateUserEvent userEvent) {
        logger.info("Send command to update user moodle id field: {}", userEvent);
        rabbitTemplate.convertAndSend(userUpdateQueue, userEvent);
    }
}
