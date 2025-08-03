package com.furkan.project.auth.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
@RequestMapping("/api/test")
public class TestController {

    // Sadece login olan herkes ulaşabilir
    @GetMapping("/public")
    public String publicAccess() {
        return "Herkese açık endpoint.";
    }

    // Sadece ROLE_USER rolüne sahip kullanıcı ulaşabilir
    @GetMapping("/user")
    @PreAuthorize("hasRole('USER')")
    public String userAccess() {
        return "USER rolü olanlar burayı görebilir.";
    }

    // Sadece ROLE_ADMIN rolüne sahip kullanıcı ulaşabilir
    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminAccess() {
        return "ADMIN rolü olanlar burayı görebilir.";
    }
}