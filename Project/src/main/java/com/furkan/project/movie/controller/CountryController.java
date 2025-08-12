package com.furkan.project.movie.controller;

import com.furkan.project.common.result.DataResult;
import com.furkan.project.common.result.PagedDataResult;
import com.furkan.project.common.result.Result;
import com.furkan.project.movie.dto.country.CountryResponse;
import com.furkan.project.movie.dto.country.CountryRequest;
import com.furkan.project.movie.service.CountryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/countries")
@RequiredArgsConstructor
public class CountryController {

    private final CountryService countryService;

    @PostMapping
    public DataResult<CountryResponse> create(@RequestBody CountryRequest request) {
        return countryService.create(request);
    }

    @GetMapping("/{id}")
    public DataResult<CountryResponse> get(@PathVariable Long id) {
        return countryService.get(id);
    }

    @GetMapping
    public PagedDataResult<CountryResponse> list(@RequestParam(required = false) String q, Pageable pageable) {
        return countryService.list(q, pageable);
    }

    @PutMapping("/{id}")
    public DataResult<CountryResponse> update(@PathVariable Long id, @RequestBody CountryRequest request) {
        return countryService.update(id, request);
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Long id) {
        return countryService.delete(id);
    }
}
