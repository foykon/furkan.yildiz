package com.example.CourseApp.dto;

import java.util.List;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
public class UserResponse {
    private Long id;
    private String username;
    private List<String> roles;

}
