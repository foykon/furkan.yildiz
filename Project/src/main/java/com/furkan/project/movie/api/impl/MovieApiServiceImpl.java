package com.furkan.project.movie.api.impl;

import com.furkan.project.movie.api.MovieApiService;
import com.furkan.project.movie.api.MovieSummary;
import com.furkan.project.movie.entity.Movie;
import com.furkan.project.movie.repository.MovieRepository; // sende movie.repository
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MovieApiServiceImpl implements MovieApiService {

    private final MovieRepository movieRepository;

    @Override
    public boolean existsById(Long movieId) {
        return movieRepository.existsById(movieId);
    }

    @Override
    public MovieSummary getSummary(Long movieId) {
        return movieRepository.findById(movieId)
                .map(this::toSummary)
                .orElse(null);
    }

    @Override
    public Map<Long, MovieSummary> getSummaries(Collection<Long> ids) {
        if (ids == null || ids.isEmpty()) return Collections.emptyMap();
        var movies = movieRepository.findAllById(ids);
        return movies.stream()
                .map(this::toSummary)
                .collect(Collectors.toMap(MovieSummary::id, Function.identity(), (a,b)->a, LinkedHashMap::new));
    }

    @Override
    public Set<Long> filterIdsByTitleWithin(Collection<Long> candidateIds, String q) {
        if (candidateIds == null || candidateIds.isEmpty() || q == null || q.isBlank()) return Set.of();
        return movieRepository.findIdsByIdInAndTitleLikeIgnoreCase(candidateIds, q);
    }

    private MovieSummary toSummary(Movie m) {
        Integer year = (m.getReleaseDate() != null) ? m.getReleaseDate().getYear() : null;
        return new MovieSummary(m.getId(), m.getTitle(), m.getPosterUrl(), year);
    }
}
