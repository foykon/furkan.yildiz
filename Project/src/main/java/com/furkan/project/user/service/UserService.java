package com.furkan.project.user.service;

import com.furkan.project.common.result.DataResult;
import com.furkan.project.common.result.PagedDataResult;
import com.furkan.project.common.result.Result;
import com.furkan.project.user.dto.request.UserFilterRequest;
import com.furkan.project.user.dto.request.UserCreateRequest;
import com.furkan.project.user.dto.request.UserUpdateRequest;
import com.furkan.project.user.dto.response.UserResponse;
import org.springframework.data.domain.Pageable;

public interface UserService {

    DataResult<UserResponse> create(UserCreateRequest request);

    DataResult<UserResponse> getById(Long id);

    PagedDataResult<UserResponse> search(UserFilterRequest filter, Pageable pageable);

    DataResult<UserResponse> update(Long id, UserUpdateRequest request);

    Result softDelete(Long id);
}