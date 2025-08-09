package com.example.CourseApp.controller;

import com.example.CourseApp.dto.CourseResponse;
import com.example.CourseApp.dto.UserRequest;
import com.example.CourseApp.dto.UserResponse;
import com.example.CourseApp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<UserResponse> getMyInfo(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(userService.getUserInfo(userDetails.getUsername()));
    }

    @GetMapping("/me/courses")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<CourseResponse>> getMyEnrolledCourses(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(userService.getUserEnrolledCourses(userDetails.getUsername()));
    }

    @PostMapping("/courses/{id}/enroll")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Void> enrollToCourse(@PathVariable Long id,
                                               @AuthenticationPrincipal UserDetails userDetails) {
        userService.enrollToCourse(userDetails.getUsername(), id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/courses/{id}/unenroll")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Void> unenrollFromCourse(@PathVariable Long id,
                                                   @AuthenticationPrincipal UserDetails userDetails) {
        userService.unenrollFromCourse(userDetails.getUsername(), id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/create")
    public ResponseEntity<UserResponse> createUser( @RequestBody UserRequest request) {
        UserResponse response = userService.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}

