package com.furkan.project.auth.controller;

import com.furkan.project.auth.dto.request.ForgotPasswordRequest;
import com.furkan.project.auth.dto.request.ResetPasswordRequest;
import com.furkan.project.auth.service.PasswordResetService;
import com.furkan.project.common.result.Result;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthPasswordController {

    private final PasswordResetService service;
    @PreAuthorize("permitAll()")
    @PostMapping("/forgot-password")
    public Result forgot(@Valid @RequestBody ForgotPasswordRequest req, HttpServletRequest http) {
        return service.forgot(req, http);
    }

    @PreAuthorize("permitAll()")
    @PostMapping("/reset-password")
    public Result reset(@Valid @RequestBody ResetPasswordRequest req) {
        return service.reset(req);
    }
}
