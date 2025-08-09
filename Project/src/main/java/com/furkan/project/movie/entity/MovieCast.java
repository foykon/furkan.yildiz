package com.furkan.project.movie.entity;

import com.furkan.project.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "movie_cast",
        uniqueConstraints = @UniqueConstraint(name = "uk_movie_actor_role",
                columnNames = {"movie_id", "actor_id", "roleName"}),
        indexes = @Index(name = "ix_movie_cast_order", columnList = "castOrder"))
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class MovieCast extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "movie_id", nullable = false)
    private Movie movie;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "actor_id", nullable = false)
    private Actor actor;

    @Column(nullable = false)
    private String roleName;

    private Integer castOrder;
}

