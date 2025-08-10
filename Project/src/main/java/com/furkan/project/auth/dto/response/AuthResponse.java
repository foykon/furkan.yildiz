package com.furkan.project.auth.dto.response;

import com.furkan.project.user.dto.response.UserResponse;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthResponse {
    private String token;
    private Long   expiresIn;
    private UserResponse user;
}
