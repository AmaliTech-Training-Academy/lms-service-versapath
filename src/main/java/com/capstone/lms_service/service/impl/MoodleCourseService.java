package com.capstone.lms_service.service.impl;

import com.capstone.lms_service.dto.MoodleCourseResponse;
import com.capstone.lms_service.service.CourseService;
import com.capstone.lms_service.util.MoodleHttpRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.common.event.CreateSkillEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MoodleCourseService implements CourseService {
    private final ObjectMapper objectMapper = new ObjectMapper(); // convert json-object
    MoodleHttpRequest moodleHttpRequest = new MoodleHttpRequest();

    @Value("${MOODLE_URL}")
    private String moodleUrl;

    @Value("${WEBSERVICE_TOKEN}")
    private String token;

    @Override
    public String createMoodleCourseStructure(CreateSkillEvent skillEvent) throws JsonProcessingException {
        return createCourse(skillEvent.getCapsuleName()).toString();
    }

    public Long createCourse(String capsuleName) throws JsonProcessingException {
        String url = moodleUrl + "?wstoken=" + token + "&wsfunction=core_course_create_courses&moodlewsrestformat=json";

        //moodle expects parameters as below
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("courses[0][fullname]", capsuleName);
        params.add("courses[0][shortname]", capsuleName);
        params.add("courses[0][categoryid]", "2");
        params.add("courses[0][visible]", "1");

        JsonNode root = moodleHttpRequest.sendRequest(params, url); // send request

        // extract courseId from success the response
        List<MoodleCourseResponse> courses = objectMapper.readValue(
                root.toString(),
                new TypeReference<>() {}
        );

        return courses.get(0).getId();
    }

}
