package com.furkan.project.movie.spec;

import com.furkan.project.movie.entity.Movie;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.util.Set;

public final class MovieSpecifications {

    private MovieSpecifications() {}

    public static Specification<Movie> notDeleted() {
        return (root, q, cb) -> cb.isFalse(root.get("deleted"));
    }

    public static Specification<Movie> titleContains(String title) {
        if (title == null || title.isBlank()) return null;
        return (root, q, cb) -> cb.like(cb.lower(root.get("title")), "%" + title.toLowerCase() + "%");
    }

    public static Specification<Movie> directorId(Long directorId) {
        if (directorId == null) return null;
        return (root, q, cb) -> cb.equal(root.get("director").get("id"), directorId);
    }

    public static Specification<Movie> genreId(Long genreId) {
        if (genreId == null) return null;
        return (root, q, cb) -> {
            var genres = root.join("genres", JoinType.LEFT);
            return cb.equal(genres.get("id"), genreId);
        };
    }
    public static Specification<Movie> ratingGte(BigDecimal min) {
        if (min == null) return null;
        return (root, q, cb) -> cb.greaterThanOrEqualTo(root.get("rating"), min);
    }
    public static Specification<Movie> ratingLte(BigDecimal max) {
        if (max == null) return null;
        return (root, q, cb) -> cb.lessThanOrEqualTo(root.get("rating"), max);
    }
    public static Specification<Movie> genreIds(Set<Long> ids) {
        if (ids == null || ids.isEmpty()) return null;
        return (root, q, cb) -> {
            q.distinct(true);
            var genres = root.join("genres", JoinType.LEFT);
            return genres.get("id").in(ids);
        };
    }

    public static Specification<Movie> languageId(Long languageId) {
        if (languageId == null) return null;
        return (root, q, cb) -> {
            var langs = root.join("languages", JoinType.LEFT);
            return cb.equal(langs.get("id"), languageId);
        };
    }

    public static Specification<Movie> languageIds(Set<Long> ids) {
        if (ids == null || ids.isEmpty()) return null;
        return (root, q, cb) -> {
            q.distinct(true);
            var langs = root.join("languages", JoinType.LEFT);
            return langs.get("id").in(ids);
        };
    }

    public static Specification<Movie> countryId(Long countryId) {
        if (countryId == null) return null;
        return (root, q, cb) -> {
            var countries = root.join("countries", JoinType.LEFT);
            return cb.equal(countries.get("id"), countryId);
        };
    }

    public static Specification<Movie> countryIds(Set<Long> ids) {
        if (ids == null || ids.isEmpty()) return null;
        return (root, q, cb) -> {
            q.distinct(true);
            var countries = root.join("countries", JoinType.LEFT);
            return countries.get("id").in(ids);
        };
    }

    public static Specification<Movie> statusEq(com.furkan.project.movie.entity.MovieStatus status) {
        if (status == null) return null;
        return (root, q, cb) -> cb.equal(root.get("status"), status);
    }

    public static Specification<Movie> contentRatingEq(com.furkan.project.movie.entity.ContentRating rating) {
        if (rating == null) return null;
        return (root, q, cb) -> cb.equal(root.get("contentRating"), rating);
    }

    public static Specification<Movie> releaseDateBetween(java.time.LocalDate from, java.time.LocalDate to) {
        if (from == null && to == null) return null;
        return (root, q, cb) -> {
            if (from != null && to != null) return cb.between(root.get("releaseDate"), from, to);
            if (from != null) return cb.greaterThanOrEqualTo(root.get("releaseDate"), from);
            return cb.lessThanOrEqualTo(root.get("releaseDate"), to);
        };
    }
}
