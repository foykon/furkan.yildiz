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
public class MovieFilterRequest {
    private String title;
    private Long directorId;
    private Long genreId;
    private Long languageId;
    private Long countryId;
    private BigDecimal minRating;
    private BigDecimal maxRating;
    private MovieStatus status;
    private ContentRating contentRating;
    private LocalDate releaseDateFrom;
    private LocalDate releaseDateTo;
    private Set<Long> genreIds;
    private Set<Long> languageIds;
    private Set<Long> countryIds;
}
