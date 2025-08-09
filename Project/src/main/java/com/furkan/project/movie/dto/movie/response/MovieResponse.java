package com.furkan.project.movie.dto.movie.response;

import com.furkan.project.movie.dto.country.CountryResponse;
import com.furkan.project.movie.dto.genre.GenreResponse;
import com.furkan.project.movie.dto.language.LanguageResponse;
import com.furkan.project.movie.dto.movie.MovieCastDTO;
import com.furkan.project.movie.entity.ContentRating;
import com.furkan.project.movie.entity.MovieStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MovieResponse {
    private Long id;
    private String title;
    private String description;
    private LocalDate releaseDate;
    private Integer duration;
    private MovieStatus status;
    private ContentRating contentRating;

    private Long directorId;
    private String directorName;

    private Set<GenreResponse> genres;
    private Set<LanguageResponse> languages;
    private Set<CountryResponse> countries;
    private Set<MovieCastDTO> cast;
}

