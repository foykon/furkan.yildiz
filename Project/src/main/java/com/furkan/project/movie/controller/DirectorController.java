package com.furkan.project.movie.controller;

import com.furkan.project.common.result.DataResult;
import com.furkan.project.common.result.PagedDataResult;
import com.furkan.project.common.result.Result;
import com.furkan.project.movie.dto.director.DirectorResponse;
import com.furkan.project.movie.dto.director.DirectorRequest;
import com.furkan.project.movie.service.DirectorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1/directors",produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class DirectorController {

    private final DirectorService directorService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public DataResult<DirectorResponse> create(@Valid @RequestBody DirectorRequest request) {
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

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public DataResult<DirectorResponse> update(@PathVariable Long id,@Valid @RequestBody DirectorRequest request) {
        return directorService.update(id, request);
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Long id) {
        return directorService.delete(id);
    }
}
