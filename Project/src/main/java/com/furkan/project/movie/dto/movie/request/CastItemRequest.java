package com.furkan.project.movie.dto.movie.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CastItemRequest {
    private Long actorId;
    private String roleName;
    private Integer castOrder;
}
