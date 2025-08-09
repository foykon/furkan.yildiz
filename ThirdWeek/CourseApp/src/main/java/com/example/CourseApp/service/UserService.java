package com.example.CourseApp.service;

import com.example.CourseApp.dto.CourseResponse;
import com.example.CourseApp.dto.UserRequest;
import com.example.CourseApp.dto.UserResponse;

import java.util.List;

public interface UserService {
    UserResponse getUserInfo(String username);

    List<CourseResponse> getUserEnrolledCourses(String username);

    void enrollToCourse(String username, Long courseId);

    void unenrollFromCourse(String username, Long courseId);
    UserResponse createUser(UserRequest request);
}
