package com.furkan.project.movie.service;

import com.furkan.project.common.result.*;
import com.furkan.project.movie.dto.director.DirectorRequest;
import com.furkan.project.movie.dto.director.DirectorResponse;
import org.springframework.data.domain.Pageable;

public interface DirectorService {
    DataResult<DirectorResponse> create(DirectorRequest request);
    DataResult<DirectorResponse> get(Long id);
    PagedDataResult<DirectorResponse> list(String q, Pageable pageable);
    DataResult<DirectorResponse> update(Long id, DirectorRequest request);
    Result delete(Long id);
}
