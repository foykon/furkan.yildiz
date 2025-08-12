package com.furkan.project.movie.dto.actor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;


@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class ActorRequest {
    @NotBlank
    @Size(max = 100)
    private String name;

    @Size(max = 50)
    private String nationality;
}
