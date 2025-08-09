package com.furkan.project.movie.service;

import com.furkan.project.common.result.*;
import com.furkan.project.movie.dto.language.LanguageRequest;
import com.furkan.project.movie.dto.language.LanguageResponse;
import org.springframework.data.domain.Pageable;

public interface LanguageService {
    DataResult<LanguageResponse> create(LanguageRequest request);
    DataResult<LanguageResponse> get(Long id);
    PagedDataResult<LanguageResponse> list(String q, Pageable pageable);
    DataResult<LanguageResponse> update(Long id, LanguageRequest request);
    Result delete(Long id);
}
