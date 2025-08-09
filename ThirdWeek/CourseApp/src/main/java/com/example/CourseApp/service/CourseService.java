package com.example.CourseApp.service;

import com.example.CourseApp.dto.CourseRequest;
import com.example.CourseApp.dto.CourseResponse;
import com.example.CourseApp.dto.UserResponse;

import java.util.List;

public interface CourseService {
    CourseResponse createCourse(CourseRequest request, String creatorUsername);

    List<CourseResponse> getAllCourses();

    CourseResponse getCourseById(Long id);

    CourseResponse updateCourse(Long courseId, CourseRequest request, String username);

    void deleteCourse(Long courseId, String username);

    List<UserResponse> getStudentsInCourse(Long courseId);
}
