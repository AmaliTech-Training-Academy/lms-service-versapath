package com.capstone.lms_service.messaging;

import com.capstone.lms_service.service.UserService;
import lombok.RequiredArgsConstructor;
import org.common.event.ErrorEvent;
import org.common.event.ProduceUserEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreateUserListener {
    private static final Logger logger = LoggerFactory.getLogger(CreateUserListener.class);
    private final UserService userService;
    private final CreateSkillErrorProducer createSkillErrorProducer;
   @RabbitListener(queues = "${USER_CREATE_QUEUE}")
    public void handleMoodleUserCreation(ProduceUserEvent userEvent){

       try {
           logger.info("Start creating user: {}", userEvent);
           userService.createUser(userEvent);
       } catch (Exception ex) {
           logger.error("Failed to create user: {}", ex.getMessage());

           // send error event
           ErrorEvent errorEvent = ErrorEvent.builder()
                   .triggeredBy(userEvent.getVersapathUserId())
                   .errorMessage(ex.getMessage())
                   .build();

           createSkillErrorProducer.sendCreateErrorSkillsCommand(errorEvent);
       }

    }
}
