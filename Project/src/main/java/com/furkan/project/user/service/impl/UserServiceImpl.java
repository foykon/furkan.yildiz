package com.furkan.project.user.service.impl;

import com.furkan.project.common.result.DataResult;
import com.furkan.project.common.result.PagedDataResult;
import com.furkan.project.common.result.Result;
import com.furkan.project.common.result.SuccessDataResult;
import com.furkan.project.common.result.SuccessResult;
import com.furkan.project.common.service.MessageService;
import com.furkan.project.user.dto.request.UserCreateRequest;
import com.furkan.project.user.dto.request.UserFilterRequest;
import com.furkan.project.user.dto.request.UserUpdateRequest;
import com.furkan.project.user.dto.response.UserResponse;
import com.furkan.project.user.entity.ERole;
import com.furkan.project.user.entity.Role;
import com.furkan.project.user.entity.User;
import com.furkan.project.user.exception.RoleNotFoundException;
import com.furkan.project.user.exception.UserNotFoundException;
import com.furkan.project.user.mapper.UserManualMapper;
import com.furkan.project.user.repository.RoleRepository;
import com.furkan.project.user.repository.UserRepository;
import com.furkan.project.user.service.UserService;
import com.furkan.project.user.spec.UserSpecifications;
import com.furkan.project.user.util.InputNormalizer;
import com.furkan.project.user.validation.UserValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private final MessageService messageService;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    private final UserValidator validator;
    private final UserManualMapper mapper;

    /* ========== CREATE ========== */
    @Override
    public DataResult<UserResponse> create(UserCreateRequest request) {
        InputNormalizer.normalize(request);
        validator.validateCreate(request);

        User user = mapper.toEntity(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        Set<ERole> desired = (request.getRoles() == null || request.getRoles().isEmpty())
                ? Set.of(ERole.ROLE_USER) : request.getRoles();
        user.setRoles(resolveRoles(desired));

        user = userRepository.save(user);
        return new SuccessDataResult<>(mapper.toResponse(user), messageService.get("user.created"));
    }

    /* ========== GET BY ID ========== */
    @Override
    @Transactional(readOnly = true)
    public DataResult<UserResponse> getById(Long id) {
        User user = userRepository.findById(id)
                .filter(u -> !u.isDeleted())
                .orElseThrow(() -> new UserNotFoundException(messageService.get("user.notfound")));

        return new SuccessDataResult<>(mapper.toResponse(user), messageService.get("user.found"));
    }

    /* ========== SEARCH (filter + pageable) ========== */
    @Override
    @Transactional(readOnly = true)
    public PagedDataResult<UserResponse> search(UserFilterRequest filterRequest, Pageable pageable) {
        var filter = InputNormalizer.normalize(filterRequest);

        boolean effectiveDeleted = (filter != null && filter.getDeleted() != null)
                ? filter.getDeleted()
                : false;

        Specification<User> spec = UserSpecifications.isDeleted(effectiveDeleted);

        if (filter != null) {
            if (filter.getUsername() != null && !filter.getUsername().isBlank()) {
                spec = spec.and(UserSpecifications.hasUsername(filter.getUsername()));
            }
            if (filter.getEmail() != null && !filter.getEmail().isBlank()) {
                spec = spec.and(UserSpecifications.hasEmail(filter.getEmail()));
            }
            if (filter.getEnabled() != null) {
                spec = spec.and(UserSpecifications.isEnabled(filter.getEnabled()));
            }
            if (filter.getLocked() != null) {
                spec = spec.and(UserSpecifications.isLocked(filter.getLocked()));
            }
        }

        Page<User> pageData = userRepository.findAll(spec, pageable);
        var items = pageData.getContent().stream().map(mapper::toResponse).toList();

        return new PagedDataResult<>(
                items,
                pageData.getNumber(),
                pageData.getSize(),
                pageData.getTotalElements(),
                pageData.getTotalPages(),
                messageService.get("user.listed")
        );
    }



    /* ========== UPDATE (PATCH) ========== */
    @Override
    public DataResult<UserResponse> update(Long id, UserUpdateRequest request) {
        InputNormalizer.normalize(request);

        User user = userRepository.findById(id)
                .filter(u -> !u.isDeleted())
                .orElseThrow(() -> new UserNotFoundException(messageService.get("user.notfound")));

        validator.validateUpdate(user, request);

        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        mapper.patch(user, request);

        if (request.getRoles() != null) {
            var target = request.getRoles().isEmpty() ? Set.of(ERole.ROLE_USER) : request.getRoles();
            user.setRoles(resolveRoles(target));
        }

        user = userRepository.save(user);
        return new SuccessDataResult<>(mapper.toResponse(user), messageService.get("user.updated"));
    }

    /* ========== SOFT DELETE ========== */
    @Override
    public Result softDelete(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(messageService.get("user.notfound")));

        if (!user.isDeleted()) {
            user.setDeleted(true);
            user.setDeletedAt(Instant.now());
            userRepository.save(user);
        }
        return new SuccessResult(messageService.get("user.deleted"));
    }

    /* ========== HELPERS ========== */
    private Set<Role> resolveRoles(Set<ERole> enums) {
        return enums.stream()
                .map(er -> roleRepository.findByName(er)
                        .orElseThrow(() -> new RoleNotFoundException(messageService.get("role.not-found"))))
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }
}
