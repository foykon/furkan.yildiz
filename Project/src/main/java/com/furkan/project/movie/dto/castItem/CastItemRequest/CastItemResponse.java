package com.furkan.project.movie.dto.castItem.CastItemRequest;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CastItemResponse {
    private Long id;
    private Long actorId;
    private String actorName;
    private String roleName;
    private Integer castOrder;
}
