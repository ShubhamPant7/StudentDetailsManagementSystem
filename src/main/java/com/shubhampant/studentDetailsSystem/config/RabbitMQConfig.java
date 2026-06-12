package com.shubhampant.studentDetailsSystem.config;

import com.shubhampant.studentDetailsSystem.constants.RabbitMQConstants;
import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Bean
    public TopicExchange studentExchange() {
        return new TopicExchange(RabbitMQConstants.STUDENT_EXCHANGE);
    }

    @Bean
    public Queue excelQueue() {
        return new Queue("excel.queue");
    }

    @Bean
    public Queue studentQueue() {
        return new Queue("student.queue");
    }

    @Bean
    public Binding excelBinding(Queue excelQueue, TopicExchange studentExchange) {
        return BindingBuilder.bind(excelQueue).to(studentExchange).with("student.excel.*");
    }

    @Bean
    public Binding studentBinding(Queue studentQueue, TopicExchange studentExchange) {
        return BindingBuilder.bind(studentQueue).to(studentExchange).with("student.*");
    }
}
