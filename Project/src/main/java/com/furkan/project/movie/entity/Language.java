package com.furkan.project.movie.entity;

import com.furkan.project.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "languages",
        indexes = @Index(name = "uk_language_iso", columnList = "isoCode", unique = true))
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Language extends BaseEntity {

    @Column(nullable = false, length = 60)
    private String name;

    @Column(nullable = false, length = 8)
    private String isoCode;

    @ManyToMany(mappedBy = "languages")
    @Builder.Default
    private Set<Movie> movies = new HashSet<>();
}

