package com.furkan.project.movie.dto.language;

import lombok.*;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class LanguageRequest {
    private String name;
    private String isoCode;
}
