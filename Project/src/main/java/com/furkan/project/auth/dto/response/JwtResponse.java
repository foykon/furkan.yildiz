package com.furkan.project.auth.dto.response;
import lombok.*;

@Data
@Builder
@RequiredArgsConstructor

public class JwtResponse {
    private String accessToken;
    private String refreshToken;
    private String tokenType= "Bearer";
    private String username;
    private String email;

    public JwtResponse(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public JwtResponse(String accessToken, String refreshToken, String tokenType, String username, String email) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.tokenType = tokenType;
        this.username = username;
        this.email = email;
    }


}
