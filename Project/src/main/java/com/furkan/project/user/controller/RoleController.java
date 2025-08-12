package com.furkan.project.user.controller;

import com.furkan.project.common.result.DataResult;
import com.furkan.project.user.dto.response.RoleResponse;
import com.furkan.project.user.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value="/api/v1/roles",produces = MediaType.APPLICATION_JSON_VALUE)
public class RoleController {

    private final RoleService roleService;

    @GetMapping
    public ResponseEntity<DataResult<List<RoleResponse>>> list() {
        return ResponseEntity.ok(roleService.findAll());
    }

    @GetMapping("/{name}")
    public ResponseEntity<DataResult<RoleResponse>> getByName(@PathVariable String name) {
        return ResponseEntity.ok(roleService.findByName(name));
    }
}
