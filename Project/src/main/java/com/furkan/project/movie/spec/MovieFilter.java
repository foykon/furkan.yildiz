package com.furkan.project.movie.spec;


import com.furkan.project.movie.entity.ContentRating;
import com.furkan.project.movie.entity.MovieStatus;

import java.time.LocalDate;
import java.util.Set;

public record MovieFilter(
        String title,
        Long directorId,
        Long genreId,
        Long languageId,
        Long countryId,
        MovieStatus status,
        ContentRating contentRating,
        LocalDate releaseDateFrom,
        LocalDate releaseDateTo,
        Set<Long> genreIds,
        Set<Long> languageIds,
        Set<Long> countryIds
) {}
