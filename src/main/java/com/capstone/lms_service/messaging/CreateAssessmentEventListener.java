package com.capstone.lms_service.messaging;

import lombok.RequiredArgsConstructor;
import org.common.event.AssessmentEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreateAssessmentEventListener {
    private static final Logger logger = LoggerFactory.getLogger(CreateAssessmentEventListener.class);

   @RabbitListener(queues = "${ASSESSMENT_CREATE_QUEUE}")
    public void createAssessment(AssessmentEvent assessmentEvent){
       try {
           logger.info("Start creating assessment: {}", assessmentEvent);

           //TODO: create assessment on Moodle
       } catch (Exception ex) {
           logger.error("Failed to create assessment: {}", ex.getMessage());
       }

   }
}
