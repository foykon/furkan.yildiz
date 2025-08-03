package com.furkan.project.auth.controller;

import com.furkan.project.auth.dto.request.RoleCreateRequest;
import com.furkan.project.auth.dto.response.RoleResponse;
import com.furkan.project.auth.service.role.IRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
public class RoleController {

    private final IRoleService roleService;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping
    public ResponseEntity<RoleResponse> createRole(@RequestBody RoleCreateRequest request) {
        return ResponseEntity.ok(roleService.createRole(request));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping
    public ResponseEntity<List<RoleResponse>> getAllRoles() {
        return ResponseEntity.ok(roleService.getAllRoles());
    }
}
