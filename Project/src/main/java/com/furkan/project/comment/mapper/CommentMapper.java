package com.furkan.project.comment.mapper;

import com.furkan.project.comment.dto.response.CommentResponse;
import com.furkan.project.comment.entity.Comment;
import org.springframework.stereotype.Component;

@Component
public class CommentMapper {
    public CommentResponse toResponse(Comment comment, String username, Long authUserId) {
        return CommentResponse.builder()
                .id(comment.getId())
                .movieId(comment.getMovieId())
                .userId(comment.getUserId())
                .username(username)
                .content(comment.getContent())
                .createdAt(comment.getCreatedAt())
                .updatedAt(comment.getUpdatedAt())
                .mine(authUserId != null && authUserId.equals(comment.getUserId()))
                .build();
    }
}
