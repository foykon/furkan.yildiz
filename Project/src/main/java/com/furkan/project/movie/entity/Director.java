package com.furkan.project.movie.entity;

import com.furkan.project.common.entity.SoftDeletableEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "directors")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Director extends SoftDeletableEntity {

    @Column(nullable = false)
    private String name;

    private String nationality;

    @OneToMany(mappedBy = "director")
    @Builder.Default
    private Set<Movie> movies = new HashSet<>();
}
