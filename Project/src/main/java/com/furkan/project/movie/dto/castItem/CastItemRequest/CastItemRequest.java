package com.furkan.project.movie.dto.castItem.CastItemRequest;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CastItemRequest {
    private Long actorId;
    private String roleName;
    private Integer castOrder;
}