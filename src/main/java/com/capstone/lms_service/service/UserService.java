package com.capstone.lms_service.service;

import com.capstone.lms_service.dto.UserRequestDto;
import com.capstone.lms_service.dto.MoodleErrorResponse;
import com.capstone.lms_service.exception.MoodleException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class UserService {
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper(); // convert json-object
    @Value("${MOODLE_URL}")
    private String moodleUrl;

    @Value("${WEBSERVICE_TOKEN}")
    private String token;
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

        // Check whether it's an object or array response before deserialization
        JsonNode root = objectMapper.readTree(response); //
        if (root.isObject() && root.has("errorcode")) {
            MoodleErrorResponse error = objectMapper.treeToValue(root, MoodleErrorResponse.class);
            throw new MoodleException(error.getMessage());
        }

        return response;
    }

}
