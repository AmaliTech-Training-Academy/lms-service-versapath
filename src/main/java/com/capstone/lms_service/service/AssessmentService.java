package com.capstone.lms_service.service;

import com.capstone.lms_service.dto.quiz.QuizAttemptDataDTO;
import com.capstone.lms_service.dto.quiz.QuizDTO;
import com.capstone.lms_service.dto.quiz.QuizSubmissionRequest;
import com.capstone.lms_service.dto.quiz.QuizSubmissionResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.common.event.AssessmentEvent;

import java.util.List;
import java.util.UUID;

public interface AssessmentService {
    void createAssessmentOnMoodle(AssessmentEvent assessmentEvent) throws JsonProcessingException;
    List<QuizDTO> getQuizzesByCourse(Long courseId) throws JsonProcessingException;
    QuizAttemptDataDTO getQuizQuestions(Long quizId, UUID userId) throws JsonProcessingException;
    QuizSubmissionResponse submitQuiz(QuizSubmissionRequest quizRequest) throws JsonProcessingException;
}
