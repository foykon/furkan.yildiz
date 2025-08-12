package com.furkan.project.movie.dto.country;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class CountryRequest {
    @NotBlank
    @Size(max = 100)
    private String name;
}
