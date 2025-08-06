package com.furkan.project.auth.service.auth;

import com.furkan.project.auth.dto.request.LoginRequest;
import com.furkan.project.auth.dto.request.RegisterRequest;
import com.furkan.project.auth.dto.request.UserRequest;
import com.furkan.project.auth.dto.response.JwtResponse;
import com.furkan.project.auth.dto.response.RegisterResponse;
import com.furkan.project.auth.dto.response.UserResponse;
import com.furkan.project.common.result.DataResult;

public interface IAuthService {
    DataResult<RegisterResponse> registerUser(RegisterRequest registerRequest);

    DataResult<JwtResponse> login(LoginRequest request);
    DataResult<JwtResponse> refreshToken(String refreshToken);

}
