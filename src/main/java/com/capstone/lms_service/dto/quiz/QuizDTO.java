package com.capstone.lms_service.dto.quiz;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class QuizDTO {
    private Long id;
    private String name;
    private String intro;
    private Integer timeLimit;
    private Integer attempts;
    private Integer grade;
    private Integer questionCount;
}
