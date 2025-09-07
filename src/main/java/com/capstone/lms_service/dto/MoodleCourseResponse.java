package com.capstone.lms_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MoodleCourseResponse {
    private Long id;
    private String shortname;
    private String fullname;
}
