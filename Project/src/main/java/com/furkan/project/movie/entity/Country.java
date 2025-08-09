package com.furkan.project.movie.entity;

import com.furkan.project.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "countries")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Country extends BaseEntity {

    @Column(nullable = false, unique = true, length = 60)
    private String name;

    @ManyToMany(mappedBy = "countries")
    @Builder.Default
    private Set<Movie> movies = new HashSet<>();
}
