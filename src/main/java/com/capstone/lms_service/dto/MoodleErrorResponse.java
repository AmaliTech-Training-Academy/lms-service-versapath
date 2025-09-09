package com.capstone.lms_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MoodleErrorResponse {
    private String exception;
    private String errorcode;
    private String message;
    private String debuginfo;
}
