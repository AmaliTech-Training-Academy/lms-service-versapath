package com.capstone.lms_service.service;

import com.capstone.lms_service.dto.AssessmentResponseDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.common.event.AssessmentEvent;

public interface AssessmentService {
    void createAssessmentOnMoodle(AssessmentEvent assessmentEvent) throws JsonProcessingException;
}
