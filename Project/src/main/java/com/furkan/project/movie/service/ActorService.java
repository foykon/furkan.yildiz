package com.furkan.project.movie.service;

import com.furkan.project.common.result.*;
import com.furkan.project.movie.dto.actor.ActorRequest;
import com.furkan.project.movie.dto.actor.ActorResponse;
import org.springframework.data.domain.Pageable;

public interface ActorService {
    DataResult<ActorResponse> create(ActorRequest request);
    DataResult<ActorResponse> get(Long id);
    PagedDataResult<ActorResponse> list(String q, Pageable pageable);
    DataResult<ActorResponse> update(Long id, ActorRequest request);
    Result delete(Long id);
}