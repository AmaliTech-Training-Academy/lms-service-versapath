package com.capstone.lms_service.service;

import com.capstone.lms_service.dto.UserRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    @Value("${MOODLE_URL}")
    private String moodleUrl;

    @Value("${WEBSERVICE_TOKEN}")
    private String token;
    public String createUser(UserRequestDto userDto) {

        String url = moodleUrl + "?wstoken=" + token + "&wsfunction=core_user_create_users&moodlewsrestformat=json";

        //moodle expects parameters
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("users[0][username]", userDto.getUsername());
        params.add("users[0][password]", userDto.getPassword());
        params.add("users[0][firstname]", userDto.getFirstname());
        params.add("users[0][lastname]", userDto.getLastname());
        params.add("users[0][email]", userDto.getEmail());
        params.add("users[0][auth]", "manual");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        HttpEntity<MultiValueMap<String, String>> request =
                new HttpEntity<>(params, headers);

        return restTemplate.postForObject(url, request, String.class);
    }

}
