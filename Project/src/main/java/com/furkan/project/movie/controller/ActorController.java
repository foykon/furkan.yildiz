package com.furkan.project.movie.controller;

import com.furkan.project.common.result.DataResult;
import com.furkan.project.common.result.PagedDataResult;
import com.furkan.project.common.result.Result;
import com.furkan.project.movie.dto.actor.ActorRequest;
import com.furkan.project.movie.dto.actor.ActorResponse;
import com.furkan.project.movie.service.ActorService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/actors")
@RequiredArgsConstructor
public class ActorController {

    private final ActorService actorService;

    @PostMapping
    public DataResult<ActorResponse> create(@RequestBody ActorRequest request) {
        return actorService.create(request);
    }

    @GetMapping("/{id}")
    public DataResult<ActorResponse> get(@PathVariable Long id) {
        return actorService.get(id);
    }

    @GetMapping
    public PagedDataResult<ActorResponse> list(@RequestParam(required = false) String q, Pageable pageable) {
        return actorService.list(q, pageable);
    }

    @PutMapping("/{id}")
    public DataResult<ActorResponse> update(@PathVariable Long id, @RequestBody ActorRequest request) {
        return actorService.update(id, request);
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Long id) {
        return actorService.delete(id);
    }
}

