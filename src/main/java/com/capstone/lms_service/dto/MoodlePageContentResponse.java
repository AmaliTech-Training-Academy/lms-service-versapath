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
@JsonIgnoreProperties(ignoreUnknown = true)
public class MoodlePageContentResponse {
    private int id; // instance id of page
    private int course; // course id
    private String name; // atom/lesson name
    private String intro;
    private int coursemodule;
    private String content;
}
