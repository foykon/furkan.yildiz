package com.furkan.project.movie.entity;

import com.furkan.project.common.entity.SoftDeletableEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "movies",
        indexes = {
                @Index(name = "ix_movie_title", columnList = "title"),
                @Index(name = "ix_movie_release_date", columnList = "releaseDate")
        })
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Movie extends SoftDeletableEntity {

    @Column(nullable = false)
    private String title;

    @Column(length = 2000)
    private String description;

    private LocalDate releaseDate;

    private BigDecimal rating;

    private String imageUrl;

    private Integer duration;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private MovieStatus status;

    @Enumerated(EnumType.STRING)
    @Column(length = 10)
    private ContentRating contentRating;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "director_id")
    private Director director;

    @ManyToMany
    @JoinTable(name = "movie_genres",
            joinColumns = @JoinColumn(name = "movie_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id"))
    @Builder.Default
    private Set<Genre> genres = new HashSet<>();

    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<MovieCast> cast = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "movie_languages",
            joinColumns = @JoinColumn(name = "movie_id"),
            inverseJoinColumns = @JoinColumn(name = "language_id"))
    @Builder.Default
    private Set<Language> languages = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "movie_countries",
            joinColumns = @JoinColumn(name = "movie_id"),
            inverseJoinColumns = @JoinColumn(name = "country_id"))
    @Builder.Default
    private Set<Country> countries = new HashSet<>();

}
