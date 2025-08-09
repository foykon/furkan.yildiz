package com.example.CourseApp.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CourseResponse {
    private Long id;
    private String title;
    private String description;
    private String createdByUsername;
}
