package com.furkan.project.movie.service.impl;

import com.furkan.project.common.result.DataResult;
import com.furkan.project.common.result.Result;
import com.furkan.project.common.result.SuccessResult;
import com.furkan.project.movie.dto.movie.request.CastItemRequest;
import com.furkan.project.movie.dto.movie.request.MovieRequest;
import com.furkan.project.movie.dto.movie.response.MovieResponse;
import com.furkan.project.movie.entity.*;
import com.furkan.project.movie.repository.*;
import com.furkan.project.movie.validation.MovieValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MovieServiceImplTest {

    @Mock MovieRepository movieRepository;
    @Mock DirectorRepository directorRepository;
    @Mock GenreRepository genreRepository;
    @Mock LanguageRepository languageRepository;
    @Mock CountryRepository countryRepository;
    @Mock ActorRepository actorRepository;
    @Mock MovieValidator movieValidator;

    @InjectMocks
    MovieServiceImpl movieService;

    Director dir;
    Genre g1; Language l1; Country c1;
    Actor a1; Actor a2;

    @BeforeEach
    void setUp() {
        dir = new Director(); dir.setId(10L); dir.setName("Nolan");
        g1 = new Genre(); g1.setId(1L); g1.setName("Sci-Fi");
        l1 = new Language(); l1.setId(5L); l1.setName("English"); l1.setIsoCode("en");
        c1 = new Country(); c1.setId(90L); c1.setName("USA");

        a1 = new Actor(); a1.setId(100L); a1.setName("Leonardo DiCaprio");
        a2 = new Actor(); a2.setId(101L); a2.setName("Joseph Gordon-Levitt");
    }

    @Test
    void create_success_appliesScalarRelationsAndCast_andMapsDetail() {
        // given
        var req = new MovieRequest();
        req.setTitle("Inception");
        req.setDescription("Dream heist");
        req.setReleaseDate(LocalDate.of(2010, 7, 16));
        req.setDuration(148);
        req.setStatus(MovieStatus.RELEASED);
        req.setContentRating(ContentRating.PG13);
        req.setImageUrl("img.jpg");
        req.setRating(BigDecimal.valueOf(8.8));

        req.setDirectorId(10L);
        req.setGenreIds(Set.of(1L));
        req.setLanguageIds(Set.of(5L));
        req.setCountryIds(Set.of(90L));

        var cast = new LinkedHashSet<CastItemRequest>();
        cast.add(new CastItemRequest(100L, "Cobb", 1));
        cast.add(new CastItemRequest(101L, "Arthur", 2));
        req.setCast(cast);

        // ✔ tek stub: validator her çağrıda success
        when(movieValidator.validateForCreateOrUpdate(any())).thenReturn(new SuccessResult());

        when(directorRepository.findById(10L)).thenReturn(Optional.of(dir));
        when(genreRepository.findAllById(Set.of(1L))).thenReturn(List.of(g1));
        when(languageRepository.findAllById(Set.of(5L))).thenReturn(List.of(l1));
        when(countryRepository.findAllById(Set.of(90L))).thenReturn(List.of(c1));
        when(actorRepository.findById(100L)).thenReturn(Optional.of(a1));
        when(actorRepository.findById(101L)).thenReturn(Optional.of(a2));

        // ID'yi set et ki sonrasında findDetailById(ID) düzgün çalışsın
        when(movieRepository.save(any(Movie.class)))
                .thenAnswer(inv -> {
                    Movie m = inv.getArgument(0);
                    if (m.getId() == null) m.setId(42L);
                    return m;
                });

        // ✔ NPE önlemek için Optional.empty() dön
        when(movieRepository.findDetailById(42L)).thenReturn(Optional.empty());

        // when
        DataResult<MovieResponse> res = movieService.create(req);

        // then
        assertThat(res.isSuccess()).isTrue();
        assertThat(res.getData().getId()).isEqualTo(42L);
        assertThat(res.getData().getDirectorName()).isEqualTo("Nolan");
        assertThat(res.getData().getGenres()).extracting("name").containsExactly("Sci-Fi");
        assertThat(res.getData().getLanguages()).extracting("isoCode").contains("en");
        assertThat(res.getData().getCountries()).extracting("name").contains("USA");
        assertThat(res.getData().getCast()).hasSize(2);

        verify(directorRepository).findById(10L);
        verify(actorRepository).findById(100L);
        verify(actorRepository).findById(101L);
        verify(movieRepository).save(any(Movie.class));
        verify(movieRepository).findDetailById(42L);
        verifyNoMoreInteractions(movieRepository);
    }

    @Test
    void update_whenMovieIsDeleted_returnsError_andDoesNotSave() {
        Movie existing = new Movie();
        existing.setId(5L);
        existing.setDeleted(true);

        when(movieRepository.findById(5L)).thenReturn(Optional.of(existing));
        when(movieValidator.validateForCreateOrUpdate(any())).thenReturn(new SuccessResult());

        DataResult<MovieResponse> res = movieService.update(5L, new MovieRequest());

        assertThat(res.isSuccess()).isFalse();
        assertThat(res.getMessage()).isEqualTo("movie.deleted");
        verify(movieRepository, never()).save(any());
    }

    @Test
    void update_replacesCast_completely() {
        Movie existing = new Movie();
        existing.setId(7L);
        existing.setDeleted(false);
        existing.setCast(new LinkedHashSet<>());

        Actor old = new Actor(); old.setId(200L); old.setName("OLD");
        MovieCast oldCast = MovieCast.builder().movie(existing).actor(old).roleName("X").castOrder(1).build();
        existing.getCast().add(oldCast);

        var req = new MovieRequest();
        req.setCast(new LinkedHashSet<>(List.of(
                new CastItemRequest(100L, "Cobb", 1),
                new CastItemRequest(101L, "Arthur", 2)
        )));

        when(movieRepository.findById(7L)).thenReturn(Optional.of(existing));
        when(movieValidator.validateForCreateOrUpdate(any())).thenReturn(new SuccessResult());
        when(actorRepository.findById(100L)).thenReturn(Optional.of(a1));
        when(actorRepository.findById(101L)).thenReturn(Optional.of(a2));
        when(movieRepository.save(any(Movie.class))).thenAnswer(inv -> inv.getArgument(0));
        when(movieRepository.findDetailById(7L)).thenReturn(Optional.of(existing));

        DataResult<MovieResponse> res = movieService.update(7L, req);

        assertThat(res.isSuccess()).isTrue();

        ArgumentCaptor<Movie> cap = ArgumentCaptor.forClass(Movie.class);
        verify(movieRepository).save(cap.capture());
        Movie saved = cap.getValue();

        assertThat(saved.getCast()).hasSize(2);
        assertThat(saved.getCast()).extracting(mc -> mc.getActor().getId())
                .containsExactlyInAnyOrder(100L, 101L);
    }

    @Test
    void softDelete_alreadyDeleted_returnsError() {
        Movie m = new Movie(); m.setId(9L); m.setDeleted(true);
        when(movieRepository.findById(9L)).thenReturn(Optional.of(m));

        Result r = movieService.softDelete(9L);

        assertThat(r.isSuccess()).isFalse();
        assertThat(r.getMessage()).isEqualTo("movie.already.deleted");
        verify(movieRepository, never()).save(any());
    }

    @Test
    void softDelete_marksDeleted_setsDeletedAt_andSaves() {
        Movie m = new Movie(); m.setId(11L); m.setDeleted(false);
        when(movieRepository.findById(11L)).thenReturn(Optional.of(m));
        when(movieRepository.save(any(Movie.class))).thenAnswer(inv -> inv.getArgument(0));

        Result r = movieService.softDelete(11L);

        assertThat(r.isSuccess()).isTrue();
        assertThat(m.isDeleted()).isTrue();
        assertThat(m.getDeletedAt()).isNotNull();
        verify(movieRepository).save(m);
    }

    @Test
    void getById_notFound_returnsErrorData() {
        when(movieRepository.findDetailById(123L)).thenReturn(Optional.empty());

        DataResult<MovieResponse> r = movieService.getById(123L);

        assertThat(r.isSuccess()).isFalse();
        assertThat(r.getMessage()).isEqualTo("movie.notfound");
    }
}
