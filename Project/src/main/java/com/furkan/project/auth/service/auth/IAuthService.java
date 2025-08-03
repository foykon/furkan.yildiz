package com.furkan.project.auth.service.auth;

import com.furkan.project.auth.dto.request.LoginRequest;
import com.furkan.project.auth.dto.response.JwtResponse;

public interface IAuthService {
    JwtResponse login(LoginRequest request);
    JwtResponse refreshToken(String refreshToken);

}
