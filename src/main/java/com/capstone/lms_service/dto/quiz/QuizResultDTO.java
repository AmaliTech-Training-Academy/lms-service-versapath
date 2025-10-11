package com.capstone.lms_service.dto.quiz;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class QuizResultDTO {
    private double grade;
    private int attemptNumber;
    private int quizId;
    private LocalDateTime timeStart;
    private LocalDateTime timeFinish;
}
