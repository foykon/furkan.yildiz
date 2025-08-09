package com.furkan.project.movie.repository;

import com.furkan.project.movie.entity.MovieCast;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MovieCastRepository extends JpaRepository<MovieCast, Long>{

    Optional<MovieCast> findByIdAndMovieId(Long id, Long movieId);
    boolean existsByMovieIdAndActorIdAndRoleNameIgnoreCase(Long movieId, Long actorId, String roleName);
    boolean existsByMovieIdAndActorId(Long movieId, Long actorId);
}
