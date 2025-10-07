package com.capstone.lms_service.service.impl;

import com.capstone.lms_service.dto.AssessmentResponseDto;
import com.capstone.lms_service.dto.quiz.AttemptDTO;
import com.capstone.lms_service.dto.quiz.QuestionDTO;
import com.capstone.lms_service.dto.quiz.QuizAttemptDataDTO;
import com.capstone.lms_service.dto.quiz.QuizDTO;
import com.capstone.lms_service.messaging.UpdateAssessmentProducer;
import com.capstone.lms_service.service.AssessmentService;
import com.capstone.lms_service.util.MoodleHttpRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.common.event.AssessmentEvent;
import org.common.event.AssessmentUpdateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MoodleAssessmentService implements AssessmentService {
    private static final Logger logger = LoggerFactory.getLogger(MoodleAssessmentService.class);
    private final MoodleHttpRequest moodleHttpRequest = new MoodleHttpRequest();
    private final UpdateAssessmentProducer updateAssessmentProducer;

    @Value("${MOODLE_URL}")
    private String moodleUrl;

    @Value("${LOCAL_CREATE_QUIZ}")
    private String token;

    @Value("${WEBSERVICE_TOKEN}")
    private String webToken;

    @Override
    public void createAssessmentOnMoodle(AssessmentEvent assessmentEvent) throws JsonProcessingException {

        String url = moodleUrl + "?wstoken=" + token
                     + "&wsfunction=local_quizapi_create_quiz&moodlewsrestformat=json";

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("courseid", String.valueOf(assessmentEvent.getMoodleCourseId()));
        params.add("name", assessmentEvent.getAssessmentName());
        params.add("intro", "Assessment Placeholder From Versapath");
        params.add("timelimit", String.valueOf(assessmentEvent.getTimeLimitMinutes() * 60)); // Moodle expects seconds
        params.add("attempts", String.valueOf(assessmentEvent.getMaxAttempts()));
        params.add("gradepass", String.valueOf(assessmentEvent.getPassingScore()));
        params.add("grade", String.valueOf(100));

        JsonNode root = moodleHttpRequest.sendRequest(params, url);

        AssessmentResponseDto responseDto = AssessmentResponseDto.builder()
                .quizId(root.get("quizid").asInt())
                .moodleCourseModuleId(root.get("coursemoduleid").asInt())
                .moodleCourseId(root.get("courseid").asInt())
                .build();

        sendEventToUpdateAssessment(responseDto, assessmentEvent);

        logger.info("Assessment created successfully in course {} with id {} and module {}", responseDto.getMoodleCourseId(),
                responseDto.getQuizId(), responseDto.getMoodleCourseModuleId());

    }

    private void sendEventToUpdateAssessment(AssessmentResponseDto assessmentResponseDto, AssessmentEvent assessmentEvent){
        AssessmentUpdateEvent assessmentUpdateEvent = AssessmentUpdateEvent.builder()
                .assessmentName(assessmentEvent.getAssessmentName())
                .moodleCourseModuleId(assessmentResponseDto.getMoodleCourseModuleId())
                .quizId(assessmentResponseDto.getQuizId())
                .build();
        updateAssessmentProducer.sendUpdateAssessments(assessmentUpdateEvent);
    }

    public List<QuizDTO> getQuizzesByCourse(Long courseId) throws JsonProcessingException {
        String url = moodleUrl + "?wstoken=" + webToken + "&wsfunction=mod_quiz_get_quizzes_by_courses&moodlewsrestformat=json";

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("courseids[0]", String.valueOf(courseId));

        JsonNode root = moodleHttpRequest.sendRequest(params, url);
        JsonNode quizzes = root.get("quizzes");

        List<QuizDTO> quizDTOList = new ArrayList<>();
        for(JsonNode quiz: quizzes){
            QuizDTO dto = QuizDTO.builder()
                    .id(quiz.get("id").asLong())
                    .name(quiz.get("name").asText())
                    .attempts(quiz.get("attempts").asInt())
                    .intro(quiz.get("intro").asText())
                    .grade(quiz.get("grade").asInt())
                    .timeLimit(quiz.get("timelimit").asInt())
                    .questionCount(quiz.get("hasquestions").asInt())
                    .build();
            quizDTOList.add(dto);

        }

        return quizDTOList;
    }

    public AttemptDTO startQuizAttempt(Long quizId) throws JsonProcessingException {
        String url = moodleUrl + "?wstoken=" + webToken + "&wsfunction=mod_quiz_start_attempt&moodlewsrestformat=json";

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("quizid", String.valueOf(quizId));

        JsonNode root = moodleHttpRequest.sendRequest(params, url);
        JsonNode attempt = root.get("attempt");

        logger.info("Starting quiz attempt for quiz ID: {}", quizId);

        return AttemptDTO.builder()
                .id(attempt.get("id").asLong())
                .userid(attempt.get("userid").asLong())
                .attempt(attempt.get("attempt").asInt())
                .quiz(attempt.get("quiz").asLong())
                .state(attempt.get("state").asText())
                .timestart(getReadableTime(attempt.get("timestart").asLong()))
                .timefinish(getReadableTime(attempt.get("timefinish").asLong()))
                .build();
    }

    public QuizAttemptDataDTO getAttemptData(Long attemptId) throws JsonProcessingException {
        String url = moodleUrl + "?wstoken=" + webToken + "&wsfunction=mod_quiz_get_attempt_data&moodlewsrestformat=json";

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("attemptid", String.valueOf(attemptId));
        params.add("page", "0"); // all questions are on the first page

        JsonNode root = moodleHttpRequest.sendRequest(params, url);

        JsonNode attempt = root.get("attempt");
        AttemptDTO attemptDTO = AttemptDTO.builder()
                .id(attempt.get("id").asLong())
                .userid(attempt.get("userid").asLong())
                .attempt(attempt.get("attempt").asInt())
                .quiz(attempt.get("quiz").asLong())
                .state(attempt.get("state").asText())
                .timestart(getReadableTime(attempt.get("timestart").asLong()))
                .timefinish(getReadableTime(attempt.get("timefinish").asLong()))
                .build();

        JsonNode questions = root.get("questions");
        List<QuestionDTO> questionDTOList = new ArrayList<>();

        for(JsonNode question: questions){
            QuestionDTO questionDTO = QuestionDTO.builder()
                    .slot(question.get("slot").asLong())
                    .type(question.get("type").toString())
                    .page(question.get("page").asInt())
                    .html(question.get("html").toString())
                    .questionid(question.get("questionnumber").asLong())
                    .build();
            questionDTOList.add(questionDTO);
        }

        logger.info("Fetching questions for attempt ID: {}", attemptId);

        return QuizAttemptDataDTO.builder()
                .attempt(attemptDTO)
                .questions(questionDTOList)
                .build();
    }

    public QuizAttemptDataDTO getQuizQuestions(Long quizId) throws JsonProcessingException {

            AttemptDTO attempt = startQuizAttempt(quizId);

            QuizAttemptDataDTO data = getAttemptData(attempt.getId());

            logger.info("Retrieved {} questions", data.getQuestions().size());
            return data;
    }

    private String getReadableTime(Long time) {
        if (time != null) {
            Instant instant = Instant.ofEpochSecond(time);
            LocalDateTime dateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
            return dateTime.toString();
        }
        return null;
    }
}
