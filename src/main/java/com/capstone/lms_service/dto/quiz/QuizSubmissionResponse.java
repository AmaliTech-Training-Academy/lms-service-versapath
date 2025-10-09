package com.capstone.lms_service.dto.quiz;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QuizSubmissionResponse {
    private String state;
    private double grade;
    private int attemptId;
}
