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
public class CreateSkillErrorProducer {
    private final RabbitTemplate rabbitTemplate;
    private static final Logger logger = LoggerFactory.getLogger(CreateSkillErrorProducer.class);
    @Value("${SKILL_CREATE_ERROR_QUEUE}")
    private String skillCreateErrorQueue;

    public void sendCreateErrorSkillsCommand(ErrorEvent errorEvent) {
        logger.info("Send command to display error: {}", errorEvent);
        rabbitTemplate.convertAndSend(skillCreateErrorQueue, errorEvent);
    }
}
