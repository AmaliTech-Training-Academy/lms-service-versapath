package com.capstone.lms_service.messaging;

import lombok.RequiredArgsConstructor;
import org.common.event.AssessmentResultEvent;
import org.common.event.AssessmentUpdateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AssessmentResultProducer {
    private final RabbitTemplate rabbitTemplate;
    private static final Logger logger = LoggerFactory.getLogger(AssessmentResultProducer.class);
    @Value("${ASSESSMENT_RESULT_QUEUE}")
    private String assessmentResultQueue;

    public void sendAssessmentResult(AssessmentResultEvent assessmentEvent) {
        logger.info("Send command to persist assessment result info: {}", assessmentEvent);
        rabbitTemplate.convertAndSend(assessmentResultQueue, assessmentEvent);
    }
}
