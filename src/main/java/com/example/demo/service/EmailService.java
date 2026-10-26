package com.example.demo.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;

    @Async("mailExecutor")
    public void sendEmail(String email, String username) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("ivangrashinton@gmail.com");
            message.setTo(email);
            message.setSubject("Welcome");
            message.setText("Welcome " + username +", akun berhasil dibuat." );
            mailSender.send(message);
            log.info("Welcome email sent to {}", email);
        } catch (Exception e) {
            log.error("Failed to send email to {}", email, e);
        }
    }
}
