package com.shubhampant.studentDetailsSystem.rabbitMQ;

import com.shubhampant.studentDetailsSystem.dto.ExcelUploadResult;
import com.shubhampant.studentDetailsSystem.entity.Student;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class RabbitMQConsumer {
    @RabbitListener(queues = "excel.queue")
    public void consumeExcel(ExcelUploadResult event) {
        log.info("Students processed: {}, Errors: {}", event.getValidStudents(), event.getErrors());
    }

    @RabbitListener(queues = "student.queue")
    public void consumeStudent(String message) {
        log.info(message);
    }
}
