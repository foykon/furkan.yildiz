package com.furkan.project.movie.dto.movie.response;

import com.furkan.project.movie.entity.ContentRating;
import com.furkan.project.movie.entity.MovieStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor @AllArgsConstructor
public class MovieListItemResponse {
    private Long id;
    private String title;
    private LocalDate releaseDate;
    private Integer duration;
    private MovieStatus status;
    private ContentRating contentRating;
    private String directorName;
    private Set<String> genreNames;
}
