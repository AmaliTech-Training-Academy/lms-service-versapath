package com.capstone.lms_service.messaging;

import com.capstone.lms_service.service.CourseService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.common.event.CreateSkillEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreateSkillEventListener {
    private static final Logger logger = LoggerFactory.getLogger(CreateSkillEventListener.class);
    private final CourseService courseService;

   @RabbitListener(queues = "versapath.skill.create")
    public void handleMoodleUserCreation(CreateSkillEvent skillEvent) throws JsonProcessingException {

        logger.info("Start creating skill event: {}", skillEvent);
        courseService.createMoodleCourseStructure(skillEvent);

    }
}
