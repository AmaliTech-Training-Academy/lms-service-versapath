package com.capstone.lms_service.controller;

import com.capstone.lms_service.dto.ClientResponseFormatDto;
import com.capstone.lms_service.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.common.event.ProduceUserEvent;
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

    @PostMapping("/create-user")
    @Operation(summary = "Insert learner on moodle", description = "This is a direct endpoint to insert leaner from Versapath to moodle ")
    public ResponseEntity<?> createUser(@RequestBody ProduceUserEvent userDto) throws JsonProcessingException {
        String responseUser = userService.createUser(userDto);
        ClientResponseFormatDto response = ClientResponseFormatDto.builder()
                .success(true)
                .message("Learner inserted successfully!")
                .errors(null)
                .data(Map.of("item", responseUser))
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
