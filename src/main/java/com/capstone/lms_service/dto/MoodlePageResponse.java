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
    private int cmid; // id for tracking progress
    private int instance; // id for fetching content
    private String name; // atom name
    private int courseid; // course/capsule id
}
