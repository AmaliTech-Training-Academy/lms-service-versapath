package com.capstone.lms_service.messaging;

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

   @RabbitListener(queues = "${SKILL_ASSIGN_QUEUE}")
    public void handleSkillAssignment(AssignAtomToCapsuleEvent skillEvent){
       try {
           logger.info("Start assigning lesson to capsule: {}", skillEvent);
           //TODO: assign lesson to a course
       } catch (Exception ex) {
           logger.error("Failed to assign lesson: {}", ex.getMessage());
       }

   }
}
