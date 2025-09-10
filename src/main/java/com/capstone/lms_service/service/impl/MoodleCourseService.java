package com.capstone.lms_service.service.impl;

import com.capstone.lms_service.dto.MoodleCourseResponse;
import com.capstone.lms_service.dto.MoodlePageResponse;
import com.capstone.lms_service.messaging.UpdateSkillProducer;
import com.capstone.lms_service.service.CourseService;
import com.capstone.lms_service.util.MoodleHttpRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.common.event.CreateSkillEvent;
import org.common.event.SkillAtom;
import org.common.event.UpdateSkillEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MoodleCourseService implements CourseService {
    private static final Logger logger = LoggerFactory.getLogger(MoodleCourseService.class);
    private final ObjectMapper objectMapper = new ObjectMapper(); // convert json-object
    private final UpdateSkillProducer updateSkillProducer;
    MoodleHttpRequest moodleHttpRequest = new MoodleHttpRequest();

    @Value("${MOODLE_URL}")
    private String moodleUrl;

    @Value("${WEBSERVICE_TOKEN}")
    private String token;

    @Value("${LOCAL_WEBSERVICE_TOKEN}")
    private String localToken;

    @Override
    public MoodleCourseResponse createMoodleCourseStructure(CreateSkillEvent skillEvent) throws JsonProcessingException {
        logger.info("inside the course {}", skillEvent);
        MoodleCourseResponse insertedCourse = createCourse(skillEvent.getCapsuleName()); // first create a course
        List<MoodlePageResponse> insertedPage = createPage(insertedCourse.getId(), skillEvent.getAtoms()); // create pages inside a course

        insertedCourse.setMoodlePages(insertedPage);
        sendEventCommandToUpdateSkillData(insertedCourse); // send updated skills data with Moodle info

        return insertedCourse;
    }

    @Override
    public String enrolLearnerInCourse(int moodleLeanerId, int moodleCourseId) throws JsonProcessingException {
        String url = moodleUrl + "?wstoken=" + token + "&wsfunction=enrol_manual_enrol_users&moodlewsrestformat=json";

        //moodle expects parameters as below
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("enrolments[0][roleid]","5"); // this is for learner
        params.add("enrolments[0][userid]",""+moodleLeanerId+"");
        params.add("enrolments[0][courseid]",""+moodleCourseId+"");

        JsonNode root = moodleHttpRequest.sendRequest(params, url); // send request
        String response = objectMapper.readValue(
                root.toString(),
                new TypeReference<>() {}); // the success response is an empty array

        logger.info("Learner enrolled successfully: {}", moodleLeanerId);

        return "Enrolled learner id "+moodleCourseId;
    }

    public MoodleCourseResponse createCourse(String capsuleName) throws JsonProcessingException {
        String url = moodleUrl + "?wstoken=" + token + "&wsfunction=core_course_create_courses&moodlewsrestformat=json";

        //moodle expects parameters as below
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("courses[0][fullname]", capsuleName);
        params.add("courses[0][shortname]", capsuleName);
        params.add("courses[0][categoryid]", "2");
        params.add("courses[0][visible]", "1");
        params.add("courses[0][summary]", "Capsule description");
        params.add("courses[0][format]", "topics");
        //Moodle stores timestamps as Unix epoch seconds
        params.add("courses[0][startdate]", String.valueOf(System.currentTimeMillis() / 1000));

        JsonNode root = moodleHttpRequest.sendRequest(params, url); // send request

        // extract courseId from success the response
        List<MoodleCourseResponse> courses = objectMapper.readValue(
                root.toString(),
                new TypeReference<>() {}
        );

        logger.info("Course created successful: {}", courses.get(0).getShortname());

        return courses.get(0);
    }

    public List<MoodlePageResponse> createPage(int courseId, List<String> atoms) throws JsonProcessingException {
        String url = moodleUrl + "?wstoken=" + localToken
                     + "&wsfunction=local_versapath_create_page&moodlewsrestformat=json";

        List<MoodlePageResponse> pageResponseList = new ArrayList<>();

        for(String atom : atoms) {

            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("courseid", ""+courseId+"");
            params.add("name", atom);
            params.add("intro", "This is a placeholder created by VersaPath.");
            params.add("content", "<p>Content to be added on Moodle</p>");

            JsonNode root = moodleHttpRequest.sendRequest(params, url); // Send request

            // Deserialize JSON to DTO
            MoodlePageResponse response = objectMapper.treeToValue(root, MoodlePageResponse.class);

            pageResponseList.add(response);
            logger.info("Page created successful: {}", response.getName());

        }


        return pageResponseList;
    }

    void sendEventCommandToUpdateSkillData(MoodleCourseResponse insertedCourse){
        logger.info("inside the send command skill {}", insertedCourse);
        List<SkillAtom> skillAtoms = new ArrayList<>();
        // map Versapath skill atom to Moodle page
        for(MoodlePageResponse page: insertedCourse.getMoodlePages()){
            SkillAtom skillAtom = SkillAtom.builder()
                    .name(page.getName())
                    .courseModuleId(page.getCmid())
                    .pageId(page.getInstance())
                    .build();

            skillAtoms.add(skillAtom);
        }
        // map Versapath skill capsule to Moodle course
        UpdateSkillEvent updateSkillEvent = UpdateSkillEvent.builder()
                .courseId(insertedCourse.getId())
                .name(insertedCourse.getShortname())
                .skillAtoms(skillAtoms)
                .build();

        updateSkillProducer.sendUpdateSkillsCommand(updateSkillEvent);
    }

}
