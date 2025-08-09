package com.furkan.project.movie.entity;

import com.furkan.project.common.entity.SoftDeletableEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "actors", indexes = @Index(name = "ix_actor_name", columnList = "name"))
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Actor extends SoftDeletableEntity {

    @Column(nullable = false)
    private String name;

    private String nationality;

    @OneToMany(mappedBy = "actor")
    @Builder.Default
    private Set<MovieCast> castCredits = new HashSet<>();
}
