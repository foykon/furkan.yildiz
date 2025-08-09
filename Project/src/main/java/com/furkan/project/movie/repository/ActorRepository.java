package com.furkan.project.movie.repository;

import com.furkan.project.movie.entity.Actor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Collection;
import java.util.Optional;

public interface ActorRepository extends JpaRepository<Actor, Long>, JpaSpecificationExecutor<Actor> {
    Optional<Actor> findByNameIgnoreCase(String name);
    long countByIdIn(Collection<Long> ids);

}
