package com.furkan.project.auth.controller;

import com.furkan.project.auth.dto.response.AuthResponse;
import com.furkan.project.auth.dto.request.LoginRequest;
import com.furkan.project.auth.service.AuthService;
import com.furkan.project.common.result.DataResult;
import com.furkan.project.common.result.Result;
import com.furkan.project.common.result.SuccessDataResult;
import com.furkan.project.common.result.SuccessResult;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<DataResult<AuthResponse>> login(
            @Valid @RequestBody LoginRequest req,
            HttpServletResponse res
    ) {
        var auth = authService.login(req, res);
        return ResponseEntity.ok(new SuccessDataResult<>(auth, "auth.success"));
    }

    @PostMapping("/refresh")
    public ResponseEntity<DataResult<AuthResponse>> refresh(
            HttpServletRequest req,
            HttpServletResponse res
    ) {
        var auth = authService.refresh(req, res);
        return ResponseEntity.ok(new SuccessDataResult<>(auth, "auth.refresh.success"));
    }

    @PostMapping("/logout")
    public ResponseEntity<Result> logout(
            HttpServletRequest req,
            HttpServletResponse res
    ) {
        authService.logout(req, res);
        return ResponseEntity.ok(new SuccessResult("auth.logout"));
    }
}
