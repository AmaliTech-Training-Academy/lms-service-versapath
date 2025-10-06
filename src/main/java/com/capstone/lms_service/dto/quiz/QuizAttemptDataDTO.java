package com.capstone.lms_service.dto.quiz;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class QuizAttemptDataDTO {
    private AttemptDTO attempt;
    private List<QuestionDTO> questions;
}
