package com.example.CourseApp.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class UserRequest {
    private String username;
    private String password;
    private List<String> roles;
}
