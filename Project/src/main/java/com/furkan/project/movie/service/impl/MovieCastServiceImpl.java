package com.furkan.project.movie.service.impl;

import com.furkan.project.common.result.*;
import com.furkan.project.movie.dto.castItem.CastItemRequest.CastItemRequest;
import com.furkan.project.movie.dto.castItem.CastItemRequest.CastItemResponse;
import com.furkan.project.movie.entity.*;
import com.furkan.project.movie.repository.*;
import com.furkan.project.movie.service.MovieCastService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class MovieCastServiceImpl implements MovieCastService {

    private final MovieRepository movieRepository;
    private final ActorRepository actorRepository;
    private final MovieCastRepository movieCastRepository;

    @Override
    public Result add(Long movieId, CastItemRequest req) {
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new IllegalArgumentException("movie.notfound"));
        Actor actor = actorRepository.findById(req.getActorId())
                .orElseThrow(() -> new IllegalArgumentException("actor.notfound"));

        if (movieCastRepository.existsByMovieIdAndActorId(movieId, actor.getId())) {
            return new ErrorResult("cast.duplicate");
        }
        MovieCast mc = MovieCast.builder()
                .movie(movie)
                .actor(actor)
                .roleName(req.getRoleName())
                .castOrder(req.getCastOrder())
                .build();

        movieCastRepository.save(mc);
        return new SuccessResult("cast.added");
    }

    @Override
    public Result update(Long movieId, Long castId, CastItemRequest req) {
        MovieCast mc = movieCastRepository.findByIdAndMovieId(castId, movieId)
                .orElseThrow(() -> new IllegalArgumentException("cast.notfound"));
        if (req.getActorId() != null) {
            Actor actor = actorRepository.findById(req.getActorId())
                    .orElseThrow(() -> new IllegalArgumentException("actor.notfound"));
            // duplicate kontrolü
            if (!mc.getActor().getId().equals(actor.getId())
                    && movieCastRepository.existsByMovieIdAndActorId(movieId, actor.getId())) {
                return new ErrorResult("cast.duplicate");
            }
            mc.setActor(actor);
        }
        if (req.getRoleName() != null && !req.getRoleName().isBlank()) {
            mc.setRoleName(req.getRoleName().trim());
        }
        if (req.getCastOrder() != null) {
            mc.setCastOrder(req.getCastOrder());
        }
        movieCastRepository.save(mc);
        return new SuccessResult("cast.updated");
    }

    @Override
    public Result remove(Long movieId, Long castId) {
        MovieCast mc = movieCastRepository.findByIdAndMovieId(castId, movieId)
                .orElseThrow(() -> new IllegalArgumentException("cast.notfound"));
        movieCastRepository.delete(mc);
        return new SuccessResult("cast.deleted");
    }

    @Override
    @Transactional(readOnly = true)
    public DataResult<List<CastItemResponse>> list(Long movieId) {
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new IllegalArgumentException("movie.notfound"));
        List<CastItemResponse> out = movie.getCast().stream()
                .sorted((a, b) -> {
                    Integer x = a.getCastOrder() == null ? Integer.MAX_VALUE : a.getCastOrder();
                    Integer y = b.getCastOrder() == null ? Integer.MAX_VALUE : b.getCastOrder();
                    return x.compareTo(y);
                })
                .map(c -> CastItemResponse.builder()
                        .id(c.getId())
                        .actorId(c.getActor().getId())
                        .actorName(c.getActor().getName())
                        .roleName(c.getRoleName())
                        .castOrder(c.getCastOrder())
                        .build())
                .toList();
        return new SuccessDataResult<>(out, "cast.listed");
    }
}
