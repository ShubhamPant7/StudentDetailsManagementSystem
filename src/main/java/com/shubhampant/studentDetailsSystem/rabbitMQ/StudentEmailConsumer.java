package com.shubhampant.studentDetailsSystem.rabbitMQ;

import com.shubhampant.studentDetailsSystem.dto.EmailDetails;
import com.shubhampant.studentDetailsSystem.entity.Student;
import com.shubhampant.studentDetailsSystem.service.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class StudentEmailConsumer {
    private final EmailService emailService;

    public StudentEmailConsumer(EmailService emailService) {
        this.emailService = emailService;
    }

    @RabbitListener(queues = "email.queue")
    public void handleDeletedStudent(EmailDetails details) {
        log.info("Sending Mailtrap email to deleted student: {}", details.getEmail());
        emailService.send(details);
    }

}
