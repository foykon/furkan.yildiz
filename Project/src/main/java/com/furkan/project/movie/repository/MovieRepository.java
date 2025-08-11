package com.furkan.project.movie.repository;


import com.furkan.project.movie.entity.Movie;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

public interface MovieRepository extends JpaRepository<Movie, Long>, JpaSpecificationExecutor<Movie> {

    // Detay sayfası için ilişkileri tek sorguda çek (N+1 önlemek için)
    @EntityGraph(attributePaths = {"director", "genres", "languages", "countries", "cast", "cast.actor"})
    @Query("select m from Movie m where m.id = :id and m.deleted = false")
    Optional<Movie> findDetailById(@Param("id") Long id);

    boolean existsByTitleIgnoreCase(String title);

    @Query("""
        select m.id
        from Movie m
        where m.id in :ids
          and lower(m.title) like lower(concat('%', :q, '%'))
    """)
    Set<Long> findIdsByIdInAndTitleLikeIgnoreCase(@Param("ids") Collection<Long> ids,
                                                  @Param("q") String q);
}