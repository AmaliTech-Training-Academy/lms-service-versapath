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
public class UpdateSkillErrorProducer {
    private final RabbitTemplate rabbitTemplate;
    private static final Logger logger = LoggerFactory.getLogger(UpdateSkillErrorProducer.class);
    @Value("${SKILL_UPDATE_ERROR_QUEUE}")
    private String skillUpdateErrorQueue;

    public void sendUpdateErrorSkillsCommand(ErrorEvent errorEvent) {
        logger.info("Send command to display error: {}", errorEvent);
        rabbitTemplate.convertAndSend(skillUpdateErrorQueue, errorEvent);
    }
}
