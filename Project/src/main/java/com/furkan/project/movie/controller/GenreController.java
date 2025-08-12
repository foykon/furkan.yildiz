package com.furkan.project.movie.controller;


import com.furkan.project.common.result.DataResult;
import com.furkan.project.common.result.PagedDataResult;
import com.furkan.project.common.result.Result;
import com.furkan.project.movie.dto.genre.GenreResponse;
import com.furkan.project.movie.dto.genre.GenreRequest;
import com.furkan.project.movie.service.GenreService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/genres")
@RequiredArgsConstructor
public class GenreController {

    private final GenreService genreService;

    @PostMapping
    public DataResult<GenreResponse> create(@RequestBody GenreRequest request) {
        return genreService.create(request);
    }

    @GetMapping("/{id}")
    public DataResult<GenreResponse> get(@PathVariable Long id) {
        return genreService.get(id);
    }

    @GetMapping
    public PagedDataResult<GenreResponse> list(@RequestParam(required = false) String q, Pageable pageable) {
        return genreService.list(q, pageable);
    }

    @PutMapping("/{id}")
    public DataResult<GenreResponse> update(@PathVariable Long id, @RequestBody GenreRequest request) {
        return genreService.update(id, request);
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Long id) {
        return genreService.delete(id);
    }
}
