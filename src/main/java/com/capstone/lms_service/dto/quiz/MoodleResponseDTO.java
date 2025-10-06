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
public class MoodleResponseDTO<T> {
    private T data;
    private String exception;
    private String errorcode;
    private String message;

    public boolean hasError() {
        return exception != null || errorcode != null;
    }
}
