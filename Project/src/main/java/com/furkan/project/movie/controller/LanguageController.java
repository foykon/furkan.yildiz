package com.furkan.project.movie.controller;

import com.furkan.project.common.result.DataResult;
import com.furkan.project.common.result.PagedDataResult;
import com.furkan.project.common.result.Result;
import com.furkan.project.movie.dto.language.LanguageResponse;
import com.furkan.project.movie.dto.language.LanguageRequest;
import com.furkan.project.movie.service.LanguageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/languages")
@RequiredArgsConstructor
public class LanguageController {

    private final LanguageService languageService;

    @PostMapping
    public DataResult<LanguageResponse> create(@RequestBody LanguageRequest request) {
        return languageService.create(request);
    }

    @GetMapping("/{id}")
    public DataResult<LanguageResponse> get(@PathVariable Long id) {
        return languageService.get(id);
    }

    @GetMapping
    public PagedDataResult<LanguageResponse> list(@RequestParam(required = false) String q, Pageable pageable) {
        return languageService.list(q, pageable);
    }

    @PutMapping("/{id}")
    public DataResult<LanguageResponse> update(@PathVariable Long id, @RequestBody LanguageRequest request) {
        return languageService.update(id, request);
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Long id) {
        return languageService.delete(id);
    }
}
