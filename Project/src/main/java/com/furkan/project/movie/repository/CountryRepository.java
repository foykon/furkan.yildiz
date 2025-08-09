package com.furkan.project.movie.repository;

import com.furkan.project.movie.entity.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Collection;
import java.util.Optional;

public interface CountryRepository extends JpaRepository<Country, Long>, JpaSpecificationExecutor<Country> {
    Optional<Country> findByNameIgnoreCase(String name);
    long countByIdIn(Collection<Long> ids);

}
