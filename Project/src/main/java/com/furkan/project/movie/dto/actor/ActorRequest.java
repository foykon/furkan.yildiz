package com.furkan.project.movie.dto.actor;

import lombok.*;


@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class ActorRequest {
    private String name;
    private String nationality;
}
