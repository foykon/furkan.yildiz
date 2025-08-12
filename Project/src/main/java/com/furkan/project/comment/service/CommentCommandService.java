package com.furkan.project.comment.service;

import com.furkan.project.comment.dto.request.CommentRequest;
import com.furkan.project.comment.dto.response.CommentResponse;

public interface CommentCommandService {
    CommentResponse create(Long movieId, CommentRequest request);
    CommentResponse update(Long movieId, Long commentId, CommentRequest request);
    void delete(Long movieId, Long commentId);
}