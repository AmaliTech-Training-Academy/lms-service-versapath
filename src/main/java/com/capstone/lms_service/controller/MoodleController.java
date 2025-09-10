package com.capstone.lms_service.controller;

import com.capstone.lms_service.dto.*;
import com.capstone.lms_service.service.CourseService;
import com.capstone.lms_service.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.common.event.CreateSkillEvent;
import org.common.event.ProduceUserEvent;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/moodle")
public class MoodleController {
    private final UserService userService;
    private final CourseService courseService;

    @PostMapping("/create-user")
    @Operation(summary = "Insert learner on moodle", description = "This is a direct endpoint to insert leaner from Versapath to moodle ")
    public ResponseEntity<?> createUser(@RequestBody ProduceUserEvent userDto) throws JsonProcessingException {
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
    @Operation(summary = "Create course on moodle", description = "This is a direct endpoint to create skill structure on Moodle ")
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

    @PostMapping("/enrol-learner")
    @Operation(summary = "Enrol learner in a course", description = "This is a direct endpoint to enrol a learner in course")
    public ResponseEntity<?> enrolLearnerInCourse(@RequestParam int moodleLearnerId, @RequestParam int moodleCourseId) throws JsonProcessingException {
        String responseCourse = courseService.enrolLearnerInCourse(moodleLearnerId, moodleCourseId);
        ClientResponseFormatDto response = ClientResponseFormatDto.builder()
                .success(true)
                .message("Learner enrolled successfully!")
                .errors(null)
                .data(Map.of("item", responseCourse))
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/fetch-content")
    @Operation(summary = "Fetch content from Moodle", description = "This is a direct endpoint to fetch lesson content")
    public ResponseEntity<?> fetchContent(@RequestParam int moodleCourseId) throws JsonProcessingException {
        List<MoodlePageContentResponse> responseCourse = courseService.fetchContent(moodleCourseId);
        ClientResponseFormatDto response = ClientResponseFormatDto.builder()
                .success(true)
                .message("Fetch contents successfully!")
                .errors(null)
                .data(Map.of("item", responseCourse))
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/fetch-single-content")
    @Operation(summary = "Fetch single content from Moodle", description = "This is a direct endpoint to fetch lesson content")
    public ResponseEntity<?> fetchSingleContent(@RequestParam int pageId) throws JsonProcessingException {
        MoodlePageContentResponse responseCourse = courseService.fetchSingleContent(pageId);
        ClientResponseFormatDto response = ClientResponseFormatDto.builder()
                .success(true)
                .message("Fetch contents successfully!")
                .errors(null)
                .data(Map.of("item", responseCourse))
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
