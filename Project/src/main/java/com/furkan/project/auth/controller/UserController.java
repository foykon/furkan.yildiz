package com.furkan.project.auth.controller;

import com.furkan.project.auth.dto.request.RoleRequest;
import com.furkan.project.auth.dto.request.RoleRequests;
import com.furkan.project.auth.dto.request.UserFilterRequest;
import com.furkan.project.auth.dto.request.UserRequest;
import com.furkan.project.auth.dto.response.UserResponse;
import com.furkan.project.auth.service.user.IUserService;
import com.furkan.project.common.result.DataResult;
import com.furkan.project.common.result.PagedDataResult;
import com.furkan.project.common.result.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.Set;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Validated
public class UserController {

    private final IUserService userService;

    @PostMapping
    @ResponseBody
    public ResponseEntity<DataResult<UserResponse>> createUser(@RequestBody @Validated UserRequest request) {
        return ResponseEntity.ok(userService.createUser(request));
    }

    @PutMapping("/{id}")
    @ResponseBody
    public ResponseEntity<DataResult<UserResponse>> updateUser(
            @PathVariable long id,
            @RequestBody @Validated UserRequest request) {
        return ResponseEntity.ok(userService.updateUser(id, request));
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    public ResponseEntity<Result> deleteUser(@PathVariable long id) {
        return ResponseEntity.ok(userService.deleteUser(id));
    }

    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<DataResult<UserResponse>> getUserById(@PathVariable long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @GetMapping
    @ResponseBody
    public ResponseEntity<DataResult<List<UserResponse>>> getAllUsers(@ModelAttribute UserFilterRequest filterRequest) {
        return ResponseEntity.ok(userService.getAllUsers(filterRequest));
    }

    @PostMapping("/add-role/{id}")
    @ResponseBody
    public ResponseEntity<DataResult<UserResponse>> addRoleToUser(@PathVariable long id, @RequestBody @Validated RoleRequests request) {
        return ResponseEntity.ok(userService.addRoleToUser(id, request));
    }


}
