package com.furkan.project.auth.service;

import com.furkan.project.auth.dto.response.AuthResponse;
import com.furkan.project.auth.dto.request.LoginRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface AuthService {
    AuthResponse login(LoginRequest request, HttpServletResponse response);
    AuthResponse refresh(HttpServletRequest request, HttpServletResponse response);
    void logout(HttpServletRequest request, HttpServletResponse response);
}