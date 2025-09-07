package com.capstone.lms_service.service.impl;

import com.capstone.lms_service.dto.MoodleUserResponse;
import com.capstone.lms_service.dto.UserRequestDto;
import com.capstone.lms_service.dto.MoodleErrorResponse;
import com.capstone.lms_service.exception.MoodleException;
import com.capstone.lms_service.messaging.UpdateUserProducer;
import com.capstone.lms_service.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.common.event.producer.UpdateUserEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MoodleUserService implements UserService {
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper(); // convert json-object
    private final UpdateUserProducer updateUserProducer;

    @Value("${MOODLE_URL}")
    private String moodleUrl;

    @Value("${WEBSERVICE_TOKEN}")
    private String token;

    @Override
    public String createUser(UserRequestDto userDto) throws JsonProcessingException {

        String url = moodleUrl + "?wstoken=" + token + "&wsfunction=core_user_create_users&moodlewsrestformat=json";

        //moodle expects parameters
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("users[0][username]", userDto.getUsername());
        params.add("users[0][password]", userDto.getPassword());
        params.add("users[0][firstname]", userDto.getFirstname());
        params.add("users[0][lastname]", userDto.getLastname());
        params.add("users[0][email]", userDto.getEmail());
        params.add("users[0][auth]", "manual");

        // set http header to send a request
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        HttpEntity<MultiValueMap<String, String>> request =
                new HttpEntity<>(params, headers);

        /* moodle success response: [{"id": 42, "username": "username"}]
           moodle error response: {"exception":"","errorcode":"","message":"","debuginfo":""}
         */

        String response = restTemplate.postForObject(url, request, String.class); // post
        JsonNode root = objectMapper.readTree(response); // handle both error and success response

        handleErrorResponse(root); // throw exception
        sendEventCommandToUpdateUserMoodleId(root); // send an event command

        return response;
    }

    private void handleErrorResponse(JsonNode root) throws JsonProcessingException {
        // Check whether it's an object or array response before deserialization
        if (root.isObject() && root.has("errorcode")) {
            MoodleErrorResponse error = objectMapper.treeToValue(root, MoodleErrorResponse.class);
            throw new MoodleException(error.getMessage());
        }

    }

    private void sendEventCommandToUpdateUserMoodleId(JsonNode root) throws JsonProcessingException {
        // extract userId from the response
        List<MoodleUserResponse> users = objectMapper.readValue(
                root.toString(),
                new TypeReference<>() {}
        );

        Long userMoodleId = users.get(0).getId();
        UUID dummyUserVersapathId =  UUID.randomUUID();

        // send event to update user moodle id
        updateUserProducer
                .sendUpdateUserMoodleIdCommand(UpdateUserEvent.builder()
                        .moodleUserId(userMoodleId)
                        .versapathUserId(dummyUserVersapathId)
                        .build());
    }

}
