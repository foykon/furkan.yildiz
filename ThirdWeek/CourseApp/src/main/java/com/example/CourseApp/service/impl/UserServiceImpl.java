package com.example.CourseApp.service.impl;

import com.example.CourseApp.dto.CourseResponse;
import com.example.CourseApp.dto.UserRequest;
import com.example.CourseApp.dto.UserResponse;
import com.example.CourseApp.entity.Course;
import com.example.CourseApp.entity.User;
import com.example.CourseApp.repository.CourseRepository;
import com.example.CourseApp.repository.UserRepository;
import com.example.CourseApp.service.UserService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;

    private final PasswordEncoder passwordEncoder;
    @PostConstruct
    public void init() {
        if (userRepository.findByUsername("furkan") == null) {
            User user = new User();
            user.setUsername("furkan");
            user.setPassword(passwordEncoder.encode("furkan"));
            user.setRoles(List.of("ROLE_USER"));
            userRepository.save(user);
        }
    }
    public UserResponse createUser(UserRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username already exists");
        }

        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .roles(List.of("USER"))
                .build();

        userRepository.save(user);

        return mapToResponse(user);
    }

    public User findEntityByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public UserResponse getUserInfo(String username) {
        User user = findEntityByUsername(username);
        return mapToResponse(user);
    }

    public List<CourseResponse> getUserEnrolledCourses(String username) {
        User user = findEntityByUsername(username);
        return user.getEnrolledCourses().stream()
                .map(this::mapCourseToResponse)
                .toList();
    }

    public void enrollToCourse(String username, Long courseId) {
        User user = findEntityByUsername(username);
        Course course = courseRepository.findById(courseId).orElseThrow(() -> new NoSuchElementException("Course not found"));
        user.getEnrolledCourses().add(course);
        userRepository.save(user);
    }

    public void unenrollFromCourse(String username, Long courseId) {
        User user = findEntityByUsername(username);
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new NoSuchElementException("Course not found"));
        user.getEnrolledCourses().remove(course);
        userRepository.save(user);
    }

    private UserResponse mapToResponse(User user) {
        UserResponse dto = new UserResponse();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setRoles(user.getRoles());
        return dto;
    }

    private CourseResponse mapCourseToResponse(Course course) {
        CourseResponse dto = new CourseResponse();
        dto.setId(course.getId());
        dto.setTitle(course.getTitle());
        dto.setDescription(course.getDescription());
        dto.setCreatedByUsername(course.getCreatedBy().getUsername());
        return dto;
    }
}
