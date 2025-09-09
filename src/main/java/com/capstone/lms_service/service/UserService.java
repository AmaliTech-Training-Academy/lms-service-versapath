package com.capstone.lms_service.service;

import com.capstone.lms_service.dto.MoodleUserResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.common.event.ProduceUserEvent;

public interface UserService {
    MoodleUserResponse createUser(ProduceUserEvent userDto) throws JsonProcessingException;
}
