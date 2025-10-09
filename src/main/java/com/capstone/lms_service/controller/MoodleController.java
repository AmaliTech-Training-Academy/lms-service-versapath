package com.capstone.lms_service.controller;

import com.capstone.lms_service.dto.*;
import com.capstone.lms_service.dto.quiz.QuizAttemptDataDTO;
import com.capstone.lms_service.dto.quiz.QuizDTO;
import com.capstone.lms_service.service.CourseService;
import com.capstone.lms_service.service.UserService;
import com.capstone.lms_service.service.impl.MoodleAssessmentService;
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
    private final MoodleAssessmentService moodleAssessmentService;

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

    @GetMapping("/assessments/{courseId}/course")
    @Operation(summary = "Fetch all the assessments by course id",
            description = "This is a direct endpoint to fetch assessment of a particular course from Moodle")
    public ResponseEntity<ClientResponseFormatDto> getQuizzesByCourse(@PathVariable Long courseId) throws JsonProcessingException {
        List<QuizDTO> quizzes = moodleAssessmentService.getQuizzesByCourse(courseId);
        ClientResponseFormatDto response = ClientResponseFormatDto.builder()
                .success(true)
                .message("Assessments data fetched successfully!")
                .errors(null)
                .data(Map.of("item", quizzes))
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("assessments/get-started")
    @Operation(summary = "Retrieve assessment questions",
            description = "This endpoint is the start of an assessment based on the attempt number and " +
                          "also retrieve questions related to the assessment")
    public ResponseEntity<ClientResponseFormatDto> startAssessment(@RequestParam Long quizId,
                                                           @RequestParam UUID userId) throws JsonProcessingException {
        QuizAttemptDataDTO quizResponse = moodleAssessmentService.getQuizQuestions(quizId, userId);
        ClientResponseFormatDto response = ClientResponseFormatDto.builder()
                .success(true)
                .message("Assessment questions retrieved successfully!")
                .errors(null)
                .data(Map.of("item", quizResponse))
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
