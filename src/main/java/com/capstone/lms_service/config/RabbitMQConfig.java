package com.capstone.lms_service.config;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;

@Configuration
public class RabbitMQConfig {

    @Value("${USER_CREATE_QUEUE}")
    private String userCreateQueue;

    @Value("${USER_UPDATE_QUEUE}")
    private String userUpdateQueue;

    @Value("${SKILL_CREATE_QUEUE}")
    private String skillCreateQueue;

    @Value("${SKILL_UPDATE_QUEUE}")
    private String skillUpdateQueue;

    @Value("${SKILL_UPDATE_ERROR_QUEUE}")
    private String skillUpdateErrorQueue;

    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory,
                                         Jackson2JsonMessageConverter messageConverter) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter);
        return template;
    }

    @Bean
    public Queue createSkillQueue() {
        return new Queue(skillCreateQueue, true);
    }

    @Bean
    public Queue updateUserQueue() {
        return new Queue(userUpdateQueue, true);
    }

    @Bean
    public Queue updateSkillQueue() {
        return new Queue(skillUpdateQueue, true);
    }

    @Bean
    public Queue updateSkillErrorQueue() {
        return new Queue(skillUpdateErrorQueue, true);
    }


    @Bean
    public Queue createUserQueue() {
        return new Queue(userCreateQueue, true);
    }


}
