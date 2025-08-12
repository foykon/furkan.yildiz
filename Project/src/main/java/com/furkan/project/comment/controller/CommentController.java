package com.furkan.project.comment.controller;

import com.furkan.project.comment.dto.request.CommentRequest;
import com.furkan.project.comment.dto.response.CommentResponse;
import com.furkan.project.comment.service.CommentCommandService;
import com.furkan.project.comment.service.CommentQueryService;
import com.furkan.project.common.result.PagedDataResult;
import com.furkan.project.common.result.SuccessDataResult;
import com.furkan.project.common.result.Result;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/movies/{movieId}/comments")
public class CommentController {

    private final CommentQueryService queryService;
    private final CommentCommandService commandService;

    @GetMapping
    public ResponseEntity<PagedDataResult<CommentResponse>> list(
            @PathVariable Long movieId,
            @ParameterObject @PageableDefault(sort = "createdAt", size = 10) Pageable pageable) {

        Page<CommentResponse> page = queryService.listByMovie(movieId, pageable);

        PagedDataResult<CommentResponse> body = new PagedDataResult<>(
                page.getContent(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                "ok"
        );

        return ResponseEntity.ok(body);
    }

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<SuccessDataResult<CommentResponse>> create(
            @PathVariable Long movieId,
            @Valid @RequestBody CommentRequest request) {

        CommentResponse data = commandService.create(movieId, request);
        return ResponseEntity.ok(new SuccessDataResult<>(data, "created"));
    }

    @PatchMapping("/{id}")
    @PreAuthorize("@commentAuth.canModify(#id)")
    public ResponseEntity<SuccessDataResult<CommentResponse>> update(
            @PathVariable Long movieId,
            @PathVariable Long id,
            @Valid @RequestBody CommentRequest request) {

        CommentResponse data = commandService.update(movieId, id, request);
        return ResponseEntity.ok(new SuccessDataResult<>(data, "updated"));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("@commentAuth.canModify(#id)")
    public ResponseEntity<Result> delete(
            @PathVariable Long movieId,
            @PathVariable Long id) {

        commandService.delete(movieId, id);
        return ResponseEntity.ok(new Result(true, "deleted"));
    }
}
