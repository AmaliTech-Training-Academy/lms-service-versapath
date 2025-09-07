package com.capstone.lms_service.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MoodlePageResponse {
    private Long cmid; // id for tracking progress
    private Long instance; // id for fetching content
    private String name; // atom name
    private Long courseid; // course/capsule id
}
