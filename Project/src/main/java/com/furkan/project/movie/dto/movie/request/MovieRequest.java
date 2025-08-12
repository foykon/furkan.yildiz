package com.furkan.project.movie.dto.movie.request;

import com.furkan.project.movie.entity.ContentRating;
import com.furkan.project.movie.entity.MovieStatus;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

@Data
@Builder
public class MovieRequest {

    @NotBlank
    @Size(max = 150)
    private String title;

    @Size(max = 1000)
    private String description;

    @NotNull
    private LocalDate releaseDate;

    @Min(1)
    @Max(600)
    private Integer duration;

    @Size(max = 500)
    private String imageUrl;

    @DecimalMin("0.0")
    @DecimalMax("10.0")
    private BigDecimal rating;

    @NotNull
    private MovieStatus status;

    private ContentRating contentRating;

    @NotNull
    private Long directorId;

    @NotEmpty
    private Set<Long> genreIds;

    @NotEmpty
    private Set<Long> languageIds;

    @NotEmpty
    private Set<Long> countryIds;

    private Set<@Valid CastItemRequest> cast;
}
