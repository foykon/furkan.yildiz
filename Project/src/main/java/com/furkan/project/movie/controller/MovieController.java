package com.furkan.project.movie.controller;

import com.furkan.project.common.result.DataResult;
import com.furkan.project.common.result.PagedDataResult;
import com.furkan.project.common.result.Result;
import com.furkan.project.movie.dto.movie.request.MovieFilterRequest;
import com.furkan.project.movie.dto.movie.request.MovieRequest;
import com.furkan.project.movie.dto.movie.response.MovieResponse;
import com.furkan.project.movie.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/movies")
@RequiredArgsConstructor
public class MovieController {

    private final MovieService movieService;

    @PostMapping
    public DataResult<MovieResponse> create(@RequestBody MovieRequest movieRequest) {
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

    @PutMapping("/{id}")
    public DataResult<MovieResponse> update(@PathVariable Long id, @RequestBody MovieRequest movieRequest) {
        return movieService.update(id, movieRequest);
    }

    @DeleteMapping("/{id}")
    public Result softDelete(@PathVariable Long id) {
        return movieService.softDelete(id);
    }
}
