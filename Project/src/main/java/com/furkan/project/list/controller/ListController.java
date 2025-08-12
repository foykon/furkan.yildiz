package com.furkan.project.list.controller;

import com.furkan.project.auth.entity.AuthUser;
import com.furkan.project.common.result.DataResult;
import com.furkan.project.common.result.PagedDataResult;
import com.furkan.project.common.result.Result;
import com.furkan.project.list.dto.request.AddListItemRequest;
import com.furkan.project.list.dto.request.ListFilterRequest;
import com.furkan.project.list.dto.request.ReorderRequest;
import com.furkan.project.list.dto.response.ListItemResponse;
import com.furkan.project.list.entity.ListType;
import com.furkan.project.list.service.ListService;
import com.furkan.project.user.api.UserApiService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping(value = "/api/v1/lists",
        produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Validated
public class ListController {

    private final ListService listService;
    private final UserApiService userApi;

    // ---- Me: token sahibinin listesi ----
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping("/me")
    public PagedDataResult<ListItemResponse> myList(
            @AuthenticationPrincipal AuthUser principal,
            @RequestParam ListType type,
            @Valid ListFilterRequest filter,
            Pageable pageable
    ) {
        Long userId = principal.getId();
        return listService.getList(userId, type, filter, pageable);
    }

    // ---- Ekle (idempotent) ----
    //@PreAuthorize("@sec.isSelfOrAdmin(#userId, authentication)")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @PostMapping("/{userId}")
    public DataResult<ListItemResponse> add(
            @PathVariable Long userId,
            @RequestBody @Valid AddListItemRequest req
    ) {
        return listService.addToList(userId, req);
    }

    // ---- Sil (idempotent soft delete) ----
    //@PreAuthorize("@sec.isSelfOrAdmin(#userId, authentication)")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @DeleteMapping("/{userId}")
    public Result remove(
            @PathVariable Long userId,
            @RequestParam Long movieId,
            @RequestParam ListType type
    ) {
        return listService.removeFromList(userId, movieId, type);
    }

    // ---- İçerir mi? ----
    //@PreAuthorize("@sec.isSelfOrAdmin(#userId, authentication)")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping("/{userId}/contains")
    public DataResult<Boolean> contains(
            @PathVariable Long userId,
            @RequestParam Long movieId,
            @RequestParam ListType type
    ) {
        return listService.contains(userId, movieId, type);
    }

    // ---- Sıralama (opsiyonel) ----
    //@PreAuthorize("@sec.isSelfOrAdmin(#userId, authentication)")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @PostMapping("/{userId}/reorder")
    public Result reorder(
            @PathVariable Long userId,
            @RequestBody @Valid ReorderRequest req
    ) {
        return listService.reorder(userId, req);
    }

    // ---- Listeyi temizle (opsiyonel) ----
    //@PreAuthorize("@sec.isSelfOrAdmin(#userId, authentication)")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @DeleteMapping("/{userId}/clear")
    public Result clear(
            @PathVariable Long userId,
            @RequestParam ListType type
    ) {
        return listService.clear(userId, type);
    }

    // ---- helper ----
    private Long resolveCurrentUserId(UserDetails principal) {
        if (principal == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "auth.required");
        }
        String username = principal.getUsername();
        Long userId = userApi.findIdByUsername(username);
        if (userId == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "user.notfound");
        }
        return userId;
    }
}