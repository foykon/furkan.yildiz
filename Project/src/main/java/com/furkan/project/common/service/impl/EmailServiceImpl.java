package com.furkan.project.common.service.impl;

import com.furkan.project.common.service.EmailService;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mail;

    @Value("${app.mail.from:${spring.mail.username}}")
    private String from;

    @Value("${app.mail.fromName:Movie Portal}")
    private String fromName;

    @Override
    public void send(String to, String subject, String htmlBody) {
        try {
            MimeMessage msg = mail.createMimeMessage();
            MimeMessageHelper h = new MimeMessageHelper(msg, "UTF-8");
            h.setFrom(new InternetAddress(from, fromName));
            h.setTo(to);
            h.setSubject(subject);
            h.setText(htmlBody, true);
            mail.send(msg);
            log.info("Mail sent to={} subject={}", to, subject);
        } catch (Exception e) {
            log.warn("email.send_failed to={} subject={} err={}", to, subject, e.toString());
        }
    }
}
