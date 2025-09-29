package com.capstone.lms_service.messaging;

import lombok.RequiredArgsConstructor;
import org.common.event.AssessmentUpdateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UpdateAssessmentProducer {
    private final RabbitTemplate rabbitTemplate;
    private static final Logger logger = LoggerFactory.getLogger(UpdateAssessmentProducer.class);
    @Value("${ASSESSMENT_UPDATE_QUEUE}")
    private String assessmentUpdateQueue;

    public void sendUpdateAssessments(AssessmentUpdateEvent assessmentEvent) {
        logger.info("Send command to update assessment data with Moodle info: {}", assessmentEvent);
        rabbitTemplate.convertAndSend(assessmentUpdateQueue, assessmentEvent);
    }
}
