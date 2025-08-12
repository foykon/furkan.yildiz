package com.furkan.project.movie.controller;

import com.furkan.project.common.result.DataResult;
import com.furkan.project.common.result.PagedDataResult;
import com.furkan.project.common.result.Result;
import com.furkan.project.movie.dto.director.DirectorResponse;
import com.furkan.project.movie.dto.director.DirectorRequest;
import com.furkan.project.movie.service.DirectorService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/directors")
@RequiredArgsConstructor
public class DirectorController {

    private final DirectorService directorService;

    @PostMapping
    public DataResult<DirectorResponse> create(@RequestBody DirectorRequest request) {
        return directorService.create(request);
    }

    @GetMapping("/{id}")
    public DataResult<DirectorResponse> get(@PathVariable Long id) {
        return directorService.get(id);
    }

    @GetMapping
    public PagedDataResult<DirectorResponse> list(@RequestParam(required = false) String q, Pageable pageable) {
        return directorService.list(q, pageable);
    }

    @PutMapping("/{id}")
    public DataResult<DirectorResponse> update(@PathVariable Long id, @RequestBody DirectorRequest request) {
        return directorService.update(id, request);
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Long id) {
        return directorService.delete(id);
    }
}
