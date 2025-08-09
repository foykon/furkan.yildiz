package com.furkan.project.movie.dto.language;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data

@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LanguageResponse {
    private Long id;
    private String name;
    private String isoCode;
}
