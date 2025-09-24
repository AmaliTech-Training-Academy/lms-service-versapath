package com.capstone.lms_service.service;

import com.capstone.lms_service.dto.MoodleCourseResponse;
import com.capstone.lms_service.dto.MoodlePageContentResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.common.event.AssignAtomToCapsuleEvent;
import org.common.event.CreateSkillEvent;

import java.util.List;

public interface CourseService {
    MoodleCourseResponse createMoodleCourseStructure(CreateSkillEvent skillEvent) throws JsonProcessingException;
    String enrolLearnerInCourse(int moodleLeanerId, int moodleCourseId) throws JsonProcessingException;
    List<MoodlePageContentResponse> fetchContent(int moodleCourseId) throws JsonProcessingException;
    MoodlePageContentResponse fetchSingleContent(int moodleCourseId) throws JsonProcessingException;
    void assignLessonToCourse(AssignAtomToCapsuleEvent skillEvent) throws JsonProcessingException;
}
