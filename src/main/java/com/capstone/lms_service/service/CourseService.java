package com.capstone.lms_service.service;

import com.capstone.lms_service.dto.MoodleCourseResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.common.event.CreateSkillEvent;

public interface CourseService {
    MoodleCourseResponse createMoodleCourseStructure(CreateSkillEvent skillEvent) throws JsonProcessingException;
    String enrolLearnerInCourse(int moodleLeanerId, int moodleCourseId) throws JsonProcessingException;
}
