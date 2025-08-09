package com.example.CourseApp.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CourseRequest {
    private String title;
    private String description;
}
