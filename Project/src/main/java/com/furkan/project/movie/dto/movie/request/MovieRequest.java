package com.furkan.project.movie.dto.movie.request;

import com.furkan.project.movie.entity.ContentRating;
import com.furkan.project.movie.entity.MovieStatus;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

@Data
@Builder
public class MovieRequest {
    private String title;
    private String description;
    private LocalDate releaseDate;
    private Integer duration;
    private String imageUrl;
    private BigDecimal rating;

    private MovieStatus status;
    private ContentRating contentRating;

    private Long directorId;
    private Set<Long> genreIds;
    private Set<Long> languageIds;
    private Set<Long> countryIds;

    private Set<CastItemRequest> cast;
}
