package com.furkan.project.movie.validation;

import com.furkan.project.common.result.ErrorResult;
import com.furkan.project.common.result.Result;
import com.furkan.project.common.result.SuccessResult;
import com.furkan.project.movie.dto.movie.request.CastItemRequest;
import com.furkan.project.movie.dto.movie.request.MovieRequest;

import com.furkan.project.movie.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class MovieValidator {

    private final DirectorRepository directorRepository;
    private final GenreRepository genreRepository;
    private final LanguageRepository languageRepository;
    private final CountryRepository countryRepository;
    private final ActorRepository actorRepository;

    public Result validateForCreateOrUpdate(MovieRequest movieRequest) {
        if (isBlank(movieRequest.getTitle())) {
            return new ErrorResult("movie.title.required");
        }
        if (movieRequest.getDuration() != null && movieRequest.getDuration() <= 0) {
            return new ErrorResult("movie.duration.invalid");
        }
        if (movieRequest.getReleaseDate() != null &&
                movieRequest.getReleaseDate().isAfter(LocalDate.now().plusYears(3))) {
            return new ErrorResult("movie.releaseDate.tooFuture");
        }

        // Director
        if (movieRequest.getDirectorId() != null &&
                directorRepository.findById(movieRequest.getDirectorId()).isEmpty()) {
            return new ErrorResult("director.notfound");
        }

        if (!existsAll(genreRepository, movieRequest.getGenreIds())) {
            return new ErrorResult("genre.ids.invalid");
        }
        if (!existsAll(languageRepository, movieRequest.getLanguageIds())) {
            return new ErrorResult("language.ids.invalid");
        }
        if (!existsAll(countryRepository, movieRequest.getCountryIds())) {
            return new ErrorResult("country.ids.invalid");
        }

        Set<CastItemRequest> castItems = movieRequest.getCast();
        if (!CollectionUtils.isEmpty(castItems)) {
            Set<Long> actorIds = castItems.stream().map(CastItemRequest::getActorId).collect(Collectors.toSet());
            if (!existsAll(actorRepository, actorIds)) {
                return new ErrorResult("actor.ids.invalid");
            }
            if (castItems.stream().anyMatch(c -> isBlank(c.getRoleName()))) {
                return new ErrorResult("cast.role.required");
            }
            boolean hasDuplicate = castItems.stream()
                    .collect(Collectors.groupingBy(
                            c -> c.getActorId() + "|" + normalize(c.getRoleName()),
                            Collectors.counting()))
                    .values().stream().anyMatch(count -> count > 1);
            if (hasDuplicate) {
                return new ErrorResult("cast.duplicate");
            }
        }

        return new SuccessResult();
    }

    private static boolean isBlank(String s) { return s == null || s.isBlank(); }
    private static String normalize(String s) { return s == null ? "" : s.trim().toLowerCase(); }

    private <E, ID> boolean existsAll(org.springframework.data.jpa.repository.JpaRepository<E, ID> repository,
                                      Collection<ID> ids) {
        if (ids == null || ids.isEmpty()) return true;
        if (repository instanceof GenreRepository gr)    return gr.countByIdIn((Collection<Long>) ids) == ids.size();
        if (repository instanceof LanguageRepository lr) return lr.countByIdIn((Collection<Long>) ids) == ids.size();
        if (repository instanceof CountryRepository cr)  return cr.countByIdIn((Collection<Long>) ids) == ids.size();
        if (repository instanceof ActorRepository ar)    return ar.countByIdIn((Collection<Long>) ids) == ids.size();
        long count = repository.count();
        return count >= 0;
    }
}
