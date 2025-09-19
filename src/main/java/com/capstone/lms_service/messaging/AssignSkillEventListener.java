package com.capstone.lms_service.messaging;

import com.capstone.lms_service.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.common.event.AssignAtomToCapsuleEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AssignSkillEventListener {
    private static final Logger logger = LoggerFactory.getLogger(AssignSkillEventListener.class);
    private final CourseService courseService;

   @RabbitListener(queues = "${SKILL_ASSIGN_QUEUE}")
    public void handleSkillAssignment(AssignAtomToCapsuleEvent skillEvent){
       try {
           logger.info("Start assigning lesson to capsule: {}", skillEvent);

           // create new lesson in a course
           courseService.createPage(skillEvent.getMoodleCourseId(), skillEvent.getAtoms());
       } catch (Exception ex) {
           logger.error("Failed to assign lesson: {}", ex.getMessage());
       }

   }
}
