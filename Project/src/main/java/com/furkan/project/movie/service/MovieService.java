package com.furkan.project.movie.service;

import com.furkan.project.common.result.DataResult;
import com.furkan.project.common.result.PagedDataResult;
import com.furkan.project.common.result.Result;
import com.furkan.project.movie.dto.movie.request.MovieFilterRequest;
import com.furkan.project.movie.dto.movie.request.MovieRequest;
import com.furkan.project.movie.dto.movie.response.MovieResponse;
import org.springframework.data.domain.Pageable;

public interface MovieService {

    DataResult<MovieResponse> create(MovieRequest request);

    DataResult<MovieResponse> getById(Long id);

    PagedDataResult<MovieResponse> search(MovieFilterRequest filter, Pageable pageable);

    DataResult<MovieResponse> update(Long id, MovieRequest request);

    Result softDelete(Long id);
}
