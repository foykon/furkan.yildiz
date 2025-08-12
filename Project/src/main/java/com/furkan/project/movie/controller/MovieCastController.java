package com.furkan.project.movie.controller;
import com.furkan.project.common.result.DataResult;
import com.furkan.project.common.result.Result;
import com.furkan.project.movie.dto.castItem.CastItemRequest.CastItemRequest;
import com.furkan.project.movie.dto.castItem.CastItemRequest.CastItemResponse;
import com.furkan.project.movie.service.MovieCastService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/movies/{movieId}/cast", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class MovieCastController {

    private final MovieCastService movieCastService;

    @GetMapping
    public DataResult<List<CastItemResponse>> list(@PathVariable Long movieId) {
        return movieCastService.list(movieId);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public Result add(@PathVariable Long movieId, @RequestBody CastItemRequest req) {
        return movieCastService.add(movieId, req);
    }

    @PutMapping(value = "/{castId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Result update(@PathVariable Long movieId,
                         @PathVariable Long castId,
                         @RequestBody CastItemRequest req) {
        return movieCastService.update(movieId, castId, req);
    }

    @DeleteMapping("/{castId}")
    public Result delete(@PathVariable Long movieId, @PathVariable Long castId) {
        return movieCastService.remove(movieId, castId);
    }
}
