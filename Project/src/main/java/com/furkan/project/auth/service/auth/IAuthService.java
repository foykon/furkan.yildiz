package com.furkan.project.auth.service.auth;

import com.furkan.project.auth.dto.request.LoginRequest;
import com.furkan.project.auth.dto.response.JwtResponse;
import com.furkan.project.auth.dto.response.UserResponse;

public interface IAuthService {
    UserResponse registerUser(String username, String email, String password);

    JwtResponse login(LoginRequest request);
    JwtResponse refreshToken(String refreshToken);

}
