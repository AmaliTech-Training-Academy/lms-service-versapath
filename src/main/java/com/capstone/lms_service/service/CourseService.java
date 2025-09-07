package com.capstone.lms_service.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.common.event.CreateSkillEvent;

public interface CourseService {
    String createMoodleCourseStructure(CreateSkillEvent skillEvent) throws JsonProcessingException;
}
