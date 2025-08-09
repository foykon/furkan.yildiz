package com.furkan.project.movie.dto.director;

import lombok.*;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class DirectorRequest {
    private String name;
    private String nationality;
}
