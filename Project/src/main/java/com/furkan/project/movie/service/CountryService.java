package com.furkan.project.movie.service;

import com.furkan.project.common.result.*;
import com.furkan.project.movie.dto.country.CountryRequest;
import com.furkan.project.movie.dto.country.CountryResponse;
import org.springframework.data.domain.Pageable;

public interface CountryService {
    DataResult<CountryResponse> create(CountryRequest request);
    DataResult<CountryResponse> get(Long id);
    PagedDataResult<CountryResponse> list(String q, Pageable pageable);
    DataResult<CountryResponse> update(Long id, CountryRequest request);
    Result delete(Long id);
}
