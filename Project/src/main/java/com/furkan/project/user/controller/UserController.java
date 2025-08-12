package com.furkan.project.user.controller;

import com.furkan.project.common.result.DataResult;
import com.furkan.project.common.result.PagedDataResult;
import com.furkan.project.common.result.Result;
import com.furkan.project.user.dto.request.UserCreateRequest;
import com.furkan.project.user.dto.request.UserFilterRequest;
import com.furkan.project.user.dto.request.UserUpdateRequest;
import com.furkan.project.user.dto.response.UserResponse;
import com.furkan.project.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/users",
        produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {

    private final UserService userService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)

    public ResponseEntity<DataResult<UserResponse>> create(@Valid @RequestBody UserCreateRequest request) {
        var result = userService.create(request);
        return ResponseEntity.status(201).body(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DataResult<UserResponse>> getById(@PathVariable Long id) {
        var result = userService.getById(id);
        return ResponseEntity.ok(result);
    }

    @GetMapping
    public ResponseEntity<PagedDataResult<UserResponse>> search(
            @Valid UserFilterRequest filter,
            @PageableDefault(size = 10) Pageable pageable
    ) {
        var result = userService.search(filter, pageable);
        return ResponseEntity.ok(result);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<DataResult<UserResponse>> update(
            @PathVariable Long id,
            @Valid @RequestBody UserUpdateRequest request
    ) {
        var result = userService.update(id, request);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Result> softDelete(@PathVariable Long id) {
        var result = userService.softDelete(id);
        return ResponseEntity.ok(result);
    }
}
