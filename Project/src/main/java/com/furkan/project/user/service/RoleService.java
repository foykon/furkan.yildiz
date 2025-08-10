package com.furkan.project.user.service;

import com.furkan.project.user.dto.response.RoleResponse;
import com.furkan.project.common.result.DataResult;

import java.util.List;

public interface RoleService {
    DataResult<List<RoleResponse>> findAll();
    DataResult<RoleResponse> findByName(String name);
}
