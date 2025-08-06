package com.furkan.project.auth.service.role.impl;

import com.furkan.project.auth.dto.request.RoleCreateRequest;
import com.furkan.project.auth.dto.response.RoleResponse;
import com.furkan.project.auth.entity.ERole;
import com.furkan.project.auth.entity.Role;
import com.furkan.project.auth.repository.RoleRepository;
import com.furkan.project.auth.service.role.IRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoleService implements IRoleService {

    private final RoleRepository roleRepository;

    @Override
    public RoleResponse createRole(RoleCreateRequest request) {
        Role role = new Role();
        role.setName(ERole.valueOf(request.getName()));

        Role saved = roleRepository.save(role);

        return RoleResponse.builder()
                .id(saved.getId())
                .name(saved.getName().name())
                .build();
    }

    @Override
    public List<RoleResponse> getAllRoles() {
        return roleRepository.findAll()
                .stream()
                .map(role -> RoleResponse.builder()
                        .id(role.getId())
                        .name(role.getName().name())
                        .build())
                .collect(Collectors.toList());
    }
}
