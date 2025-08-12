package com.furkan.project.movie.controller;

import com.furkan.project.common.result.DataResult;
import com.furkan.project.common.result.PagedDataResult;
import com.furkan.project.common.result.Result;
import com.furkan.project.movie.dto.movie.request.MovieFilterRequest;
import com.furkan.project.movie.dto.movie.request.MovieRequest;
import com.furkan.project.movie.dto.movie.response.MovieResponse;
import com.furkan.project.movie.service.MovieService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1/movies", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class MovieController {

    private final MovieService movieService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)

    public DataResult<MovieResponse> create(@Valid @RequestBody MovieRequest movieRequest) {
        return movieService.create(movieRequest);
    }

    @GetMapping("/{id}")
    public DataResult<MovieResponse> getById(@PathVariable Long id) {
        return movieService.getById(id);
    }

    @GetMapping
    public PagedDataResult<MovieResponse> search(MovieFilterRequest filter, Pageable pageable) {
        return movieService.search(filter, pageable);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public DataResult<MovieResponse> update(@PathVariable Long id,@Valid @RequestBody MovieRequest movieRequest) {
        return movieService.update(id, movieRequest);
    }

    @DeleteMapping("/{id}")
    public Result softDelete(@PathVariable Long id) {
        return movieService.softDelete(id);
    }
}
