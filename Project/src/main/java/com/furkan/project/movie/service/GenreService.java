package com.furkan.project.movie.service;

import com.furkan.project.common.result.*;
import com.furkan.project.movie.dto.genre.GenreRequest;
import com.furkan.project.movie.dto.genre.GenreResponse;
import org.springframework.data.domain.Pageable;

public interface GenreService {
    DataResult<GenreResponse> create(GenreRequest request);
    DataResult<GenreResponse> get(Long id);
    PagedDataResult<GenreResponse> list(String q, Pageable pageable);
    DataResult<GenreResponse> update(Long id, GenreRequest request);
    Result delete(Long id);
}
