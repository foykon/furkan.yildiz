package com.furkan.project.user.service.impl;

import com.furkan.project.auth.exception.RoleNotFoundException;
import com.furkan.project.common.result.DataResult;
import com.furkan.project.common.result.SuccessDataResult;
import com.furkan.project.common.service.MessageService;
import com.furkan.project.user.dto.response.RoleResponse;
import com.furkan.project.user.entity.ERole;
import com.furkan.project.user.entity.Role;
import com.furkan.project.user.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RoleServiceImpl implements com.furkan.project.user.service.RoleService {

    private final RoleRepository roleRepository;
    private final MessageService messageService;

    @Override
    public DataResult<List<RoleResponse>> findAll() {
        List<RoleResponse> roles = roleRepository.findAll().stream()
                .map(this::toDto)
                .toList();
        return new SuccessDataResult<>(roles, messageService.get("role.listed"));
    }

    @Override
    public DataResult<RoleResponse> findByName(String name) {
        ERole en = ERole.valueOf(name);
        Role role = roleRepository.findByName(en)
                .orElseThrow(() -> new RoleNotFoundException(messageService.get("role.not-found")));
        return new SuccessDataResult<>(toDto(role), messageService.get("role.found"));
    }

    private RoleResponse toDto(Role r) {
        return RoleResponse.builder()
                .id(r.getId())
                .name(r.getName())
                .description(r.getDescription())
                .build();
    }
}
