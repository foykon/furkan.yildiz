package com.furkan.project.auth.service;

import com.furkan.project.auth.dto.request.ForgotPasswordRequest;
import com.furkan.project.auth.dto.request.ResetPasswordRequest;
import com.furkan.project.common.result.Result;

import jakarta.servlet.http.HttpServletRequest;

public interface PasswordResetService {
    Result forgot(ForgotPasswordRequest req, HttpServletRequest http);
    Result reset(ResetPasswordRequest req);
}