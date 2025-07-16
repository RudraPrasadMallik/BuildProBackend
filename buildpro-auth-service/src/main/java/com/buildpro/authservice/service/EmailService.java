package com.buildpro.authservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    public void sendVerificationEmail(String toEmail, String token) {
        String subject = "Verify Your Email";
        String verificationUrl = "http://localhost:8081/auth/verify-email?token=" + token;
        String body = "Click the link to verify your email:\n" + verificationUrl;

        sendEmail(toEmail, subject, body);
    }

    public void sendResetPasswordEmail(String toEmail, String token) {
        String subject = "Reset Your Password";
        String resetUrl = "http://localhost:8081/auth/reset-password?token=" + token;
        String body = "Click the link to reset your password:\n" + resetUrl;

        sendEmail(toEmail, subject, body);
    }

    private void sendEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
    }
    
    
    
    public void sendPasswordResetEmail(String toEmail, String token) {
        String resetUrl = "http://localhost:8080/auth/reset-password?token=" + token;
        String subject = "Reset Your Password";
        String body = "Click the link to reset your password: " + resetUrl;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject(subject);
        message.setText(body);
        mailSender.send(message);
    }

}
