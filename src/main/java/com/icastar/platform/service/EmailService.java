package com.icastar.platform.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    public void sendOtpEmail(String toEmail, String otp) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(toEmail);
            message.setSubject("iCastar - OTP Verification");
            message.setText("Your OTP for iCastar verification is: " + otp + 
                          "\n\nThis OTP is valid for 5 minutes." +
                          "\n\nIf you didn't request this OTP, please ignore this email." +
                          "\n\nBest regards,\nThe iCastar Team");

            mailSender.send(message);
            log.info("OTP email sent successfully to: {}", toEmail);
        } catch (Exception e) {
            log.error("Failed to send OTP email to: {}", toEmail, e);
            // In production, you might want to throw an exception or handle this differently
        }
    }

    public void sendWelcomeEmail(String toEmail, String firstName) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(toEmail);
            message.setSubject("Welcome to iCastar!");
            message.setText("Dear " + firstName + ",\n\n" +
                          "Welcome to iCastar - your gateway to the entertainment industry!\n\n" +
                          "Your account has been successfully created. You can now:\n" +
                          "- Create your professional profile\n" +
                          "- Browse and apply for jobs\n" +
                          "- Connect with recruiters\n" +
                          "- Showcase your talent\n\n" +
                          "If you have any questions, feel free to contact our support team.\n\n" +
                          "Best regards,\nThe iCastar Team");

            mailSender.send(message);
            log.info("Welcome email sent successfully to: {}", toEmail);
        } catch (Exception e) {
            log.error("Failed to send welcome email to: {}", toEmail, e);
        }
    }

    public void sendJobAlertEmail(String toEmail, String artistName, String jobTitle, String companyName) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(toEmail);
            message.setSubject("New Job Alert - " + jobTitle);
            message.setText("Dear " + artistName + ",\n\n" +
                          "A new job opportunity that matches your profile has been posted:\n\n" +
                          "Job Title: " + jobTitle + "\n" +
                          "Company: " + companyName + "\n\n" +
                          "Log in to your iCastar account to view details and apply.\n\n" +
                          "Best regards,\nThe iCastar Team");

            mailSender.send(message);
            log.info("Job alert email sent successfully to: {}", toEmail);
        } catch (Exception e) {
            log.error("Failed to send job alert email to: {}", toEmail, e);
        }
    }
}
