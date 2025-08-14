package com.furkan.project.movie.service.impl;

import com.furkan.project.common.logging.LogExecution;
import com.furkan.project.common.result.*;
import com.furkan.project.common.service.MessageService;
import com.furkan.project.movie.dto.country.CountryResponse;
import com.furkan.project.movie.dto.genre.GenreResponse;
import com.furkan.project.movie.dto.language.LanguageResponse;
import com.furkan.project.movie.dto.movie.MovieCastDTO;
import com.furkan.project.movie.dto.movie.request.CastItemRequest;
import com.furkan.project.movie.dto.movie.request.MovieFilterRequest;
import com.furkan.project.movie.dto.movie.request.MovieRequest;
import com.furkan.project.movie.dto.movie.response.MovieResponse;
import com.furkan.project.movie.entity.*;
import com.furkan.project.movie.repository.*;
import com.furkan.project.movie.service.MovieService;
import com.furkan.project.movie.validation.MovieValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static com.furkan.project.movie.spec.MovieSpecifications.*;

@Service
@RequiredArgsConstructor
@Transactional
@LogExecution
public class MovieServiceImpl implements MovieService {

    private final MovieRepository movieRepository;
    private final DirectorRepository directorRepository;
    private final GenreRepository genreRepository;
    private final LanguageRepository languageRepository;
    private final CountryRepository countryRepository;
    private final ActorRepository actorRepository;
    private final MessageService messages;
    private final MovieValidator movieValidator;

    // ============== CREATE ==============
    @Override
    public DataResult<MovieResponse> create(MovieRequest movieRequest) {
        Result validation = movieValidator.validateForCreateOrUpdate(movieRequest);
        if (!validation.isSuccess()) {
            return new ErrorDataResult<>(null, validation.getMessage());
        }

        try {
            Movie movie = new Movie();
            applyUpsert(movie, movieRequest);
            movie = movieRepository.save(movie);

            Movie detail = movieRepository.findDetailById(movie.getId()).orElse(movie);
            return new SuccessDataResult<>(toResponse(detail), messages.get("movie.created"));
        } catch (Exception ex) {
            return new ErrorDataResult<>(null, messages.get("movie.create.failed"));
        }
    }

    // ============== GET ==============
    @Override
    @Transactional(readOnly = true)
    public DataResult<MovieResponse> getById(Long id) {
        var optionalMovie = movieRepository.findDetailById(id);
        if (optionalMovie.isEmpty()) {
            return new ErrorDataResult<>(null, messages.get("movie.notfound"));
        }
        return new SuccessDataResult<>(toResponse(optionalMovie.get()), messages.get("movie.found"));
    }

    // ============== SEARCH (paged) ==============
    @Override
    @Transactional(readOnly = true)
    public PagedDataResult<MovieResponse> search(MovieFilterRequest filter, Pageable pageable) {
        Specification<Movie> specification = Specification.allOf(
                notDeleted(),
                titleContains(filter.getTitle()),
                directorId(filter.getDirectorId()),
                genreId(filter.getGenreId()),
                genreIds(filter.getGenreIds()),
                languageId(filter.getLanguageId()),
                languageIds(filter.getLanguageIds()),
                countryId(filter.getCountryId()),
                countryIds(filter.getCountryIds()),
                statusEq(filter.getStatus()),
                ratingGte(filter.getMinRating()),
                ratingLte(filter.getMaxRating()),
                contentRatingEq(filter.getContentRating()),
                releaseDateBetween(filter.getReleaseDateFrom(), filter.getReleaseDateTo())
        );

        Page<Movie> page = movieRepository.findAll(specification, pageable);

        var items = page.getContent().stream()
                .map(this::toResponseLite)
                .collect(Collectors.toList());

        return new PagedDataResult<>(
                items,
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                true,
                messages.get("movie.search.ok")
        );
    }

    // ============== UPDATE ==============
    @Override
    public DataResult<MovieResponse> update(Long id, MovieRequest movieRequest) {
        Result validation = movieValidator.validateForCreateOrUpdate(movieRequest);
        if (!validation.isSuccess()) {
            return new ErrorDataResult<>(null, validation.getMessage());
        }

        try {
            Movie movie = movieRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException(messages.get("movie.notfound")));

            if (Boolean.TRUE.equals(movie.isDeleted())) {
                return new ErrorDataResult<>(null, messages.get("movie.deleted"));
            }

            applyUpsert(movie, movieRequest);
            movie = movieRepository.save(movie);

            Movie detail = movieRepository.findDetailById(movie.getId()).orElse(movie);
            return new SuccessDataResult<>(toResponse(detail), messages.get("movie.updated"));
        } catch (Exception ex) {
            return new ErrorDataResult<>(null, messages.get("movie.update.failed"));
        }
    }

    // ============== SOFT DELETE ==============
    @Override
    public Result softDelete(Long id) {
        return movieRepository.findById(id)
                .map(movie -> {
                    if (Boolean.TRUE.equals(movie.isDeleted())) {
                        return new ErrorResult(messages.get("movie.already.deleted"));
                    }
                    movie.setDeleted(true);
                    movie.setDeletedAt(Instant.now());
                    movieRepository.save(movie);
                    return new SuccessResult(messages.get("movie.deleted.ok"));
                })
                .orElseGet(() -> new ErrorResult(messages.get("movie.notfound")));
    }

    // =========================================================
    // ================= Helpers / Mapping =====================
    // =========================================================

    private void applyUpsert(Movie movie, MovieRequest movieRequest) {
        applyScalarFields(movie, movieRequest);
        applyRelations(movie, movieRequest);
        replaceCast(movie, movieRequest.getCast());
    }

    private void applyScalarFields(Movie movie, MovieRequest movieRequest) {
        if (movieRequest.getTitle() != null)          movie.setTitle(movieRequest.getTitle());
        if (movieRequest.getDescription() != null)    movie.setDescription(movieRequest.getDescription());
        if (movieRequest.getReleaseDate() != null)    movie.setReleaseDate(movieRequest.getReleaseDate());
        if (movieRequest.getDuration() != null)       movie.setDuration(movieRequest.getDuration());
        if (movieRequest.getStatus() != null)         movie.setStatus(movieRequest.getStatus());
        if (movieRequest.getContentRating() != null)  movie.setContentRating(movieRequest.getContentRating());
        if (movieRequest.getImageUrl() != null)       movie.setImageUrl(movieRequest.getImageUrl());
        if (movieRequest.getRating() != null)         movie.setRating(movieRequest.getRating());

    }

    private void applyRelations(Movie movie, MovieRequest movieRequest) {
        if (movieRequest.getDirectorId() != null) {
            movie.setDirector(fetchDirector(movieRequest.getDirectorId()));
        }
        if (movieRequest.getGenreIds() != null) {
            movie.setGenres(fetchGenres(movieRequest.getGenreIds()));
        }
        if (movieRequest.getLanguageIds() != null) {
            movie.setLanguages(fetchLanguages(movieRequest.getLanguageIds()));
        }
        if (movieRequest.getCountryIds() != null) {
            movie.setCountries(fetchCountries(movieRequest.getCountryIds()));
        }
    }

    private void replaceCast(Movie movie, Set<CastItemRequest> castRequests) {
        if (castRequests == null) return;
        movie.getCast().clear();
        for (CastItemRequest castRequest : castRequests) {
            Actor actor = fetchActor(castRequest.getActorId());
            MovieCast movieCast = MovieCast.builder()
                    .movie(movie)
                    .actor(actor)
                    .roleName(castRequest.getRoleName())
                    .castOrder(castRequest.getCastOrder())
                    .build();
            movie.getCast().add(movieCast);
        }
    }

    private Director fetchDirector(Long directorId) {
        return directorRepository.findById(directorId)
                .orElseThrow(() -> new IllegalArgumentException(messages.get("director.notfound")));
    }

    private Set<Genre> fetchGenres(Set<Long> genreIds) {
        return new HashSet<>(genreRepository.findAllById(genreIds));
    }

    private Set<Language> fetchLanguages(Set<Long> languageIds) {
        return new HashSet<>(languageRepository.findAllById(languageIds));
    }

    private Set<Country> fetchCountries(Set<Long> countryIds) {
        return new HashSet<>(countryRepository.findAllById(countryIds));
    }

    private Actor fetchActor(Long actorId) {
        return actorRepository.findById(actorId)
                .orElseThrow(() -> new IllegalArgumentException(messages.get("actor.notfound:") + actorId));
    }

    private MovieResponse toResponse(Movie movie) {
        MovieResponse response = new MovieResponse();
        response.setId(movie.getId());
        response.setTitle(movie.getTitle());
        response.setDescription(movie.getDescription());
        response.setReleaseDate(movie.getReleaseDate());
        response.setDuration(movie.getDuration());
        response.setStatus(movie.getStatus());
        response.setContentRating(movie.getContentRating());
        response.setImageUrl(movie.getImageUrl());
        response.setContentRating(movie.getContentRating());
        response.setRating(movie.getRating());


        if (movie.getDirector() != null) {
            response.setDirectorId(movie.getDirector().getId());
            response.setDirectorName(movie.getDirector().getName());
        }

        response.setGenres(movie.getGenres().stream()
                .map(g -> new GenreResponse(g.getId(), g.getName()))
                .collect(Collectors.toSet()));

        response.setLanguages(movie.getLanguages().stream()
                .map(l -> new LanguageResponse(l.getId(), l.getName(), l.getIsoCode()))
                .collect(Collectors.toSet()));

        response.setCountries(movie.getCountries().stream()
                .map(c -> new CountryResponse(c.getId(), c.getName()))
                .collect(Collectors.toSet()));

        response.setCast(movie.getCast().stream()
                .map(mc -> new MovieCastDTO(
                        mc.getId(),
                        mc.getActor().getId(),
                        mc.getActor().getName(),
                        mc.getRoleName(),
                        mc.getCastOrder()
                ))
                .collect(Collectors.toSet()));

        return response;
    }

    private MovieResponse toResponseLite(Movie movie) {
        return toResponse(movie);
    }
}
