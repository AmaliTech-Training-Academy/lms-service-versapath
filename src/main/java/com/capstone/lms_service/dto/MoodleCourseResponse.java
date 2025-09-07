package com.capstone.lms_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MoodleCourseResponse {
    private Long id;
    private String shortname;
    private String fullname;
    List<MoodlePageResponse> moodlePages;
}
