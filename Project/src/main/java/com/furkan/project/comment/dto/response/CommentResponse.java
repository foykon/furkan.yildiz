package com.furkan.project.comment.dto.response;

import lombok.Builder;
import lombok.Value;

import java.time.Instant;

@Value
@Builder
public class CommentResponse {
    Long id;
    Long movieId;
    Long userId;
    String username;
    String content;
    Instant createdAt;
    Instant updatedAt;
    boolean mine;
}
