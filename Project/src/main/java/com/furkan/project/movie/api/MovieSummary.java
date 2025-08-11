package com.furkan.project.movie.api;

public record MovieSummary(
        Long id,
        String title,
        String posterUrl,
        Integer releaseYear
) {}
