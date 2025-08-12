package com.furkan.project.movie.controller;

import com.furkan.project.common.result.DataResult;
import com.furkan.project.common.result.PagedDataResult;
import com.furkan.project.common.result.Result;
import com.furkan.project.movie.dto.country.CountryResponse;
import com.furkan.project.movie.dto.country.CountryRequest;
import com.furkan.project.movie.service.CountryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1/countries",
        produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class CountryController {

    private final CountryService countryService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)

    public DataResult<CountryResponse> create(@Valid @RequestBody CountryRequest request) {
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

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public DataResult<CountryResponse> update(@PathVariable Long id, @Valid @RequestBody CountryRequest request) {
        return countryService.update(id, request);
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Long id) {
        return countryService.delete(id);
    }
}
