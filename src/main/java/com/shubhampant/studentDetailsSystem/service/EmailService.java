package com.shubhampant.studentDetailsSystem.service;

import com.shubhampant.studentDetailsSystem.dto.EmailDetails;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    private final JavaMailSender javaMailSender;

    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public String send(EmailDetails details) {
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();

            mailMessage.setTo(details.getEmail());
            mailMessage.setSubject(details.getMsgSubject());
            mailMessage.setText(details.getMsgBody());

            javaMailSender.send(mailMessage);
            return "Mail Sent Successfully";
        } catch (Exception e) {
            return "Error while sending message";
        }
    }
}
