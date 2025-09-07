package com.capstone.lms_service.controller;

import com.capstone.lms_service.dto.ClientResponseFormatDto;
import com.capstone.lms_service.dto.MoodleCourseResponse;
import com.capstone.lms_service.dto.MoodleUserResponse;
import com.capstone.lms_service.dto.UserRequestDto;
import com.capstone.lms_service.service.CourseService;
import com.capstone.lms_service.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.common.event.CreateSkillEvent;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/moodle")
public class MoodleController {
    private final UserService userService;
    private final CourseService courseService;

    @PostMapping("/create-user")
    @Operation(summary = "Insert learner on moodle", description = "This is a direct endpoint to insert leaner from Versapath to moodle ")
    public ResponseEntity<?> createUser(@RequestBody UserRequestDto userDto) throws JsonProcessingException {
        MoodleUserResponse responseUser = userService.createUser(userDto);
        ClientResponseFormatDto response = ClientResponseFormatDto.builder()
                .success(true)
                .message("Learner inserted successfully!")
                .errors(null)
                .data(Map.of("item", responseUser))
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/create-course")
    @Operation(summary = "Insert learner on moodle", description = "This is a direct endpoint to insert leaner from Versapath to moodle ")
    public ResponseEntity<?> createCourse(@RequestBody CreateSkillEvent skillEvent) throws JsonProcessingException {
        MoodleCourseResponse responseCourse = courseService.createMoodleCourseStructure(skillEvent);
        ClientResponseFormatDto response = ClientResponseFormatDto.builder()
                .success(true)
                .message("Course inserted successfully!")
                .errors(null)
                .data(Map.of("item", responseCourse))
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
