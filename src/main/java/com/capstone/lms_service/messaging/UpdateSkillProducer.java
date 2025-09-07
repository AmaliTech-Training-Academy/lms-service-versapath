package com.capstone.lms_service.messaging;

import lombok.RequiredArgsConstructor;
import org.common.event.UpdateSkillEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UpdateSkillProducer {
    private final RabbitTemplate rabbitTemplate;
    private static final Logger logger = LoggerFactory.getLogger(UpdateSkillProducer.class);

    public void sendUpdateSkillsCommand(UpdateSkillEvent skillEvent) {
        logger.info("Send command to update skill data with Moodle info: {}", skillEvent.getName());
        rabbitTemplate.convertAndSend("versapath.skill.update", skillEvent);
    }
}
