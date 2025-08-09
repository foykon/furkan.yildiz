package com.furkan.project.movie.repository;


import com.furkan.project.movie.entity.Movie;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MovieRepository extends JpaRepository<Movie, Long>, JpaSpecificationExecutor<Movie> {

    // Detay sayfası için ilişkileri tek sorguda çek (N+1 önlemek için)
    @EntityGraph(attributePaths = {"director", "genres", "languages", "countries", "cast", "cast.actor"})
    @Query("select m from Movie m where m.id = :id and m.deleted = false")
    Optional<Movie> findDetailById(@Param("id") Long id);

    boolean existsByTitleIgnoreCase(String title);
}