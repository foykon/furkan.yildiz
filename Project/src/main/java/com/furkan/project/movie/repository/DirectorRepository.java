package com.furkan.project.movie.repository;


import com.furkan.project.movie.entity.Director;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface DirectorRepository extends JpaRepository<Director, Long>, JpaSpecificationExecutor<Director> {
    Optional<Director> findByNameIgnoreCase(String name);
}