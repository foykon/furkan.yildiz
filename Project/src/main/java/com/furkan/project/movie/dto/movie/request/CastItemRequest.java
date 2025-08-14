package com.furkan.project.movie.dto.movie.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CastItemRequest {
    private Long actorId;
    private String roleName;
    private Integer castOrder;
}
