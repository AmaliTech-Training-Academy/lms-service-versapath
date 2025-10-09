package com.capstone.lms_service.dto.quiz;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QuizSubmissionRequest {
    private UUID userId;
    private int attemptId;
    private int totalQuestions;
    private int qubaId; // question usage id
    private List<QuizAnswerDto> answers;
}
