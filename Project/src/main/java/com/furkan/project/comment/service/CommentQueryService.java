package com.furkan.project.comment.service;

import com.furkan.project.comment.dto.response.CommentResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CommentQueryService {
    Page<CommentResponse> listByMovie(Long movieId, Pageable pageable);
}