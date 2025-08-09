package com.furkan.project.movie.service;

import com.furkan.project.common.result.DataResult;
import com.furkan.project.common.result.Result;
import com.furkan.project.movie.dto.castItem.CastItemRequest.CastItemRequest;
import com.furkan.project.movie.dto.castItem.CastItemRequest.CastItemResponse;

import java.util.List;

public interface MovieCastService {
    Result add(Long movieId, CastItemRequest req);
    Result update(Long movieId, Long castId, CastItemRequest req);
    Result remove(Long movieId, Long castId);
    DataResult<List<CastItemResponse>> list(Long movieId);
}
