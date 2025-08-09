package com.example.CourseApp.controller;

import com.example.CourseApp.dto.CourseRequest;
import com.example.CourseApp.dto.CourseResponse;
import com.example.CourseApp.dto.UserResponse;
import com.example.CourseApp.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;

    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CourseResponse> createCourse(@RequestBody CourseRequest request,
                                                       @AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();
        CourseResponse response = courseService.createCourse(request, username);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<CourseResponse>> getAllCourses() {
        return ResponseEntity.ok(courseService.getAllCourses());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourseResponse> getCourseById(@PathVariable Long id) {
        return ResponseEntity.ok(courseService.getCourseById(id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CourseResponse> updateCourse(@PathVariable Long id,
                                                       @RequestBody CourseRequest request,
                                                       @AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();
        CourseResponse updated = courseService.updateCourse(id, request, username);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteCourse(@PathVariable Long id,
                                             @AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();
        courseService.deleteCourse(id, username);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/students")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserResponse>> getStudentsInCourse(@PathVariable Long id) {
        return ResponseEntity.ok(courseService.getStudentsInCourse(id));
    }
}

