package com.furkan.project.movie.dto.movie;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data@Builder@NoArgsConstructor
@AllArgsConstructor
public class MovieCastDTO {
    private Long castId;
    private Long actorId;
    private String actorName;
    private String roleName;
    private Integer castOrder;
}
