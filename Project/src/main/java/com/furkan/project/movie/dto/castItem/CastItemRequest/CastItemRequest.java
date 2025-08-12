package com.furkan.project.movie.dto.castItem.CastItemRequest;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CastItemRequest {
    @NotNull
    private Long actorId;

    @NotBlank
    @Size(max = 100)
    private String roleName;

    @Min(0)
    @Max(1000)
    private Integer castOrder;
}