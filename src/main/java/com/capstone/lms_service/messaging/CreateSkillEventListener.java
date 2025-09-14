package com.capstone.lms_service.messaging;

import com.capstone.lms_service.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.common.event.CreateSkillEvent;
import org.common.event.ErrorEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreateSkillEventListener {
    private static final Logger logger = LoggerFactory.getLogger(CreateSkillEventListener.class);
    private final CourseService courseService;
    private final CreateSkillErrorProducer createSkillErrorProducer;

   @RabbitListener(queues = "${SKILL_CREATE_QUEUE}")
    public void handleMoodleUserCreation(CreateSkillEvent skillEvent){
       try {
           logger.info("Start creating skill event: {}", skillEvent);
           courseService.createMoodleCourseStructure(skillEvent);
       } catch (Exception ex) {
           logger.error("Failed to create skill: {}", ex.getMessage());

           // send error event
           ErrorEvent errorEvent = ErrorEvent.builder()
                   .triggeredBy(skillEvent.getUserId())
                   .errorMessage(ex.getMessage())
                   .build();

           createSkillErrorProducer.sendCreateErrorSkillsCommand(errorEvent);;
       }

   }
}
