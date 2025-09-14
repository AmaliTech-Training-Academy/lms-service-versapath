package com.capstone.lms_service.util;

import com.capstone.lms_service.dto.MoodleErrorResponse;
import com.capstone.lms_service.exception.MoodleException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.UUID;

@RequiredArgsConstructor
public class MoodleHttpRequest {
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper(); // convert json-object


    public JsonNode sendRequest(MultiValueMap<String, String> params , String url) throws JsonProcessingException {
        // set http header to send a request
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

        /* moodle success response example : [{"id": 42, "shortname": "fullname"}]
           moodle error response: {"exception":"","errorcode":"","message":"","debuginfo":""}
         */

        String response = restTemplate.postForObject(url, request, String.class); // post
        JsonNode root = objectMapper.readTree(response); // handle both error and success response

        handleErrorResponse(root); // throw exception if encounter error

        return root; // return success response
    }

    private void handleErrorResponse(JsonNode root) throws JsonProcessingException {
        // Check whether it's an object or array response before deserialization
        if (root.isObject() && root.has("errorcode")) {
            MoodleErrorResponse error = objectMapper.treeToValue(root, MoodleErrorResponse.class);

            throw new MoodleException(error.getMessage()); // this is used when there is an HTTP request
        }

    }
}
