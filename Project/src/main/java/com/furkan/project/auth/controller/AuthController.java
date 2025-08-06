package com.furkan.project.auth.controller;


import com.furkan.project.auth.dto.request.LoginRequest;
import com.furkan.project.auth.dto.request.RegisterRequest;
import com.furkan.project.auth.dto.request.TokenRefreshRequest;
import com.furkan.project.auth.dto.request.UserRequest;
import com.furkan.project.auth.dto.response.JwtResponse;
import com.furkan.project.auth.dto.response.RegisterResponse;
import com.furkan.project.auth.dto.response.UserResponse;
import com.furkan.project.auth.security.JwtTokenProvider;
import com.furkan.project.auth.service.auth.IAuthService;
import com.furkan.project.auth.service.user.IUserService;
import com.furkan.project.common.result.DataResult;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final IAuthService authService;

    @PostMapping("/register")
    public ResponseEntity<DataResult<RegisterResponse>> registerUser(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.registerUser(request));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
            return ResponseEntity.ok(authService.login(request));

    }

    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(@Valid @RequestBody TokenRefreshRequest request) {
                return ResponseEntity.ok(authService.refreshToken(request.getRefreshToken()));

    }

}