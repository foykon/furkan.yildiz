package com.furkan.project.movie.repository;

import com.furkan.project.movie.entity.Language;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Collection;
import java.util.Optional;

public interface LanguageRepository extends JpaRepository<Language, Long>, JpaSpecificationExecutor<Language> {
    Optional<Language> findByIsoCode(String isoCode);
    long countByIdIn(Collection<Long> ids);

}
