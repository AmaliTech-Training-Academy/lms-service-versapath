package com.capstone.lms_service.controller;

import com.capstone.lms_service.dto.UserRequestDto;
import com.capstone.lms_service.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/moodle")
public class MoodleController {
    private final UserService userService;

    @PostMapping("/create-user")
    public String createUser(@RequestBody UserRequestDto userDto) {
        return userService.createUser(userDto);
    }

}
