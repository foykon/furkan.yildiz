package com.furkan.project.auth.service.role;

import com.furkan.project.auth.dto.request.RoleCreateRequest;
import com.furkan.project.auth.dto.response.RoleResponse;

import java.util.List;

public interface IRoleService {
    RoleResponse createRole(RoleCreateRequest request);
    List<RoleResponse> getAllRoles();
}
