package com.furkan.project.movie.dto.director;


import lombok.*;

@Data @NoArgsConstructor @AllArgsConstructor
public class DirectorResponse {
    private Long id;
    private String name;
    private String nationality;
}