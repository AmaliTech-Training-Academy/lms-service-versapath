package com.capstone.lms_service.service;

import com.capstone.lms_service.dto.MoodleUserResponse;
import com.capstone.lms_service.dto.UserRequestDto;
import com.fasterxml.jackson.core.JsonProcessingException;

public interface UserService {
    MoodleUserResponse createUser(UserRequestDto userDto) throws JsonProcessingException;
}
