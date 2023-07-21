package com.example.DepartmentalStoreCrud.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailSenderService {
    @Autowired
    private JavaMailSender mailSender;

    /**
     * To send an email on order
     *
     * @param toEmail - Recipient email id
     * @param subject - Email subject
     * @param body - Email content
     */
    public void sendSimpleEmail(final String toEmail, final String subject, final String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("gurjotloveparmar@gmail.com");
        message.setTo(toEmail);
        message.setText(body);
        message.setSubject(subject);
        mailSender.send(message);
    }
}
