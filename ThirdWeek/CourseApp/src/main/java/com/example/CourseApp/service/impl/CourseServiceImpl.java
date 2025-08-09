package com.example.CourseApp.service.impl;

import com.example.CourseApp.dto.CourseRequest;
import com.example.CourseApp.dto.CourseResponse;
import com.example.CourseApp.dto.UserResponse;
import com.example.CourseApp.entity.Course;
import com.example.CourseApp.entity.User;
import com.example.CourseApp.repository.CourseRepository;
import com.example.CourseApp.repository.UserRepository;
import com.example.CourseApp.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;
    private final UserRepository userRepository;

    public CourseResponse createCourse(CourseRequest request, String creatorUsername) {
        User creator = userRepository.findByUsername(creatorUsername);

        Course course = new Course();
        course.setTitle(request.getTitle());
        course.setDescription(request.getDescription());
        course.setCreatedBy(creator);

        Course saved = courseRepository.save(course);
        return mapToResponse(saved);
    }

    public List<CourseResponse> getAllCourses() {
        return courseRepository.findAll().stream()
                .map(this::mapToResponse)
                .toList();
    }

    public CourseResponse getCourseById(Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Course not found"));
        return mapToResponse(course);
    }

    public CourseResponse updateCourse(Long courseId, CourseRequest request, String username) {
        Course existing = courseRepository.findById(courseId)
                .orElseThrow(() -> new NoSuchElementException("Course not found"));

        if (!existing.getCreatedBy().getUsername().equals(username)) {
            throw new AccessDeniedException("You can only update your own courses.");
        }

        existing.setTitle(request.getTitle());
        existing.setDescription(request.getDescription());

        Course updated = courseRepository.save(existing);
        return mapToResponse(updated);
    }

    public void deleteCourse(Long courseId, String username) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new NoSuchElementException("Course not found"));

        if (!course.getCreatedBy().getUsername().equals(username)) {
            throw new AccessDeniedException("You can only delete your own courses.");
        }

        courseRepository.delete(course);
    }

    public List<UserResponse> getStudentsInCourse(Long courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new NoSuchElementException("Course not found"));

        return course.getStudents().stream()
                .map(this::mapUserToResponse)
                .toList();
    }

    private CourseResponse mapToResponse(Course course) {
        CourseResponse dto = new CourseResponse();
        dto.setId(course.getId());
        dto.setTitle(course.getTitle());
        dto.setDescription(course.getDescription());
        dto.setCreatedByUsername(course.getCreatedBy().getUsername());
        return dto;
    }

    private UserResponse mapUserToResponse(User user) {
        UserResponse dto = new UserResponse();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setRoles(user.getRoles());
        return dto;
    }
}
