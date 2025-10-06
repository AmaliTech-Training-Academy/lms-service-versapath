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
public class QuestionDTO {
    private Long slot;
    private Long questionid;
    private String type;
    private String html;
    private Double maxmark;
    private Integer page;
    private String questiontext;
    private List<OptionDTO> options;
}
