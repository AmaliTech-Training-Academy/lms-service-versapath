package com.capstone.lms_service.service.impl;

import com.capstone.lms_service.dto.AssessmentResponseDto;
import com.capstone.lms_service.service.AssessmentService;
import com.capstone.lms_service.util.MoodleHttpRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.common.event.AssessmentEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Service
@RequiredArgsConstructor
public class MoodleAssessmentService implements AssessmentService {
    private static final Logger logger = LoggerFactory.getLogger(MoodleAssessmentService.class);
    private final MoodleHttpRequest moodleHttpRequest = new MoodleHttpRequest();

    @Value("${MOODLE_URL}")
    private String moodleUrl;

    @Value("${LOCAL_CREATE_QUIZ}")
    private String token;

    @Override
    public void createAssessmentOnMoodle(AssessmentEvent assessmentEvent) throws JsonProcessingException {

        String url = moodleUrl + "?wstoken=" + token
                     + "&wsfunction=local_quizapi_create_quiz&moodlewsrestformat=json";

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("courseid", String.valueOf(assessmentEvent.getMoodleCourseId()));
        params.add("name", assessmentEvent.getAssessmentName());
        params.add("intro", "Assessment Placeholder From Versapath");
        params.add("timelimit", String.valueOf(assessmentEvent.getTimeLimitMinutes() * 60)); // Moodle expects seconds
        params.add("attempts", String.valueOf(assessmentEvent.getMaxAttempts()));
        params.add("gradepass", String.valueOf(assessmentEvent.getPassingScore()));
        params.add("grade", String.valueOf(100));

        JsonNode root = moodleHttpRequest.sendRequest(params, url);

        AssessmentResponseDto responseDto = AssessmentResponseDto.builder()
                .quizId(root.get("quizid").asInt())
                .moodleCourseModuleId(root.get("coursemoduleid").asInt())
                .moodleCourseId(root.get("courseid").asInt())
                .build();

        logger.info("Assessment created successfully in course {} with id {} and module {}", responseDto.getMoodleCourseId(),
                responseDto.getQuizId(), responseDto.getMoodleCourseModuleId());

    }
}
