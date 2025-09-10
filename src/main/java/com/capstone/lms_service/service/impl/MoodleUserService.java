package com.capstone.lms_service.service.impl;

import com.capstone.lms_service.dto.MoodleUserResponse;
import com.capstone.lms_service.dto.UserRequestDto;
import com.capstone.lms_service.messaging.UpdateUserProducer;
import com.capstone.lms_service.service.UserService;
import com.capstone.lms_service.util.MoodleHttpRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.common.event.ProduceUserEvent;
import org.common.event.UpdateUserEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MoodleUserService implements UserService {
    MoodleHttpRequest moodleHttpRequest = new MoodleHttpRequest();
    private final ObjectMapper objectMapper = new ObjectMapper(); // convert json-object
    private final UpdateUserProducer updateUserProducer;

    @Value("${MOODLE_URL}")
    private String moodleUrl;

    @Value("${WEBSERVICE_TOKEN}")
    private String token;

    @Override
    public MoodleUserResponse createUser(ProduceUserEvent userDto) throws JsonProcessingException {

        String url = moodleUrl + "?wstoken=" + token + "&wsfunction=core_user_create_users&moodlewsrestformat=json";

        // create default password if is not set
        String defaultPassword = userDto.getPassword() == null ? userDto.getUsername()+String
                .valueOf(userDto.getUsername()
                .charAt(0)).toUpperCase()+"1!" : userDto.getPassword();
        //moodle expects parameters
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("users[0][username]", userDto.getUsername());
        params.add("users[0][password]", defaultPassword);
        params.add("users[0][firstname]", userDto.getUsername());
        params.add("users[0][lastname]", userDto.getLastName());
        params.add("users[0][email]", userDto.getEmail());
        params.add("users[0][auth]", "manual");

        JsonNode root = moodleHttpRequest.sendRequest(params, url); // send request

        // extract inserted user from success response
        List<MoodleUserResponse> users = objectMapper.readValue(
                root.toString(),
                new TypeReference<>() {}
        );
        sendEventCommandToUpdateUserMoodleId(users.get(0)); // send an event command

        return users.get(0); // return the inserted user
    }

    private void sendEventCommandToUpdateUserMoodleId(MoodleUserResponse user) {

        Long userMoodleId = user.getId();
        UUID dummyUserVersapathId =  UUID.randomUUID();

        // send event to update user moodle id
        updateUserProducer
                .sendUpdateUserMoodleIdCommand(UpdateUserEvent.builder()
                        .moodleUserId(userMoodleId)
                        .versapathUserId(dummyUserVersapathId)
                        .build());
    }

}
