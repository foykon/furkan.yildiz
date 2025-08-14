package com.furkan.project.common.service;


public interface EmailService {
    void send(String to, String subject, String htmlBody);
}