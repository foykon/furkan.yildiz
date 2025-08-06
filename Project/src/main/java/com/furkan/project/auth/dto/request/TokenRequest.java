package com.furkan.project.auth.dto.request;

import lombok.Data;

@Data
public class TokenRequest {
    private String refreshToken;
}
