package com.furkan.project.movie.dto.actor;

import lombok.*;

@Data @NoArgsConstructor @AllArgsConstructor
public class ActorResponse {
    private Long id;
    private String name;
    private String nationality;
}
