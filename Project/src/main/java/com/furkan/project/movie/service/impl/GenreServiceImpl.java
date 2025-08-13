package com.furkan.project.movie.service.impl;

import com.furkan.project.common.logging.LogExecution;
import com.furkan.project.common.result.*;
import com.furkan.project.movie.dto.genre.GenreRequest;
import com.furkan.project.movie.dto.genre.GenreResponse;
import com.furkan.project.movie.entity.Genre;
import com.furkan.project.movie.repository.GenreRepository;
import com.furkan.project.movie.service.GenreService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@LogExecution

public class GenreServiceImpl implements GenreService {

    private final GenreRepository genreRepository;

    @Override
    public DataResult<GenreResponse> create(GenreRequest request) {
        if (request.getName() == null || request.getName().isBlank()) {
            return new ErrorDataResult<GenreResponse>(null, "genre.name.required");
        }
        Genre saved = genreRepository.save(Genre.builder()
                .name(request.getName().trim())
                .build());

        return new SuccessDataResult<>(new GenreResponse(saved.getId(), saved.getName()), "genre.created");
    }

    @Override
    @Transactional(readOnly = true)
    public DataResult<GenreResponse> get(Long id) {
        var optional = genreRepository.findById(id);
        if (optional.isEmpty()) {
            return new ErrorDataResult<GenreResponse>(null, "genre.notfound");
        }
        Genre genre = optional.get();
        return new SuccessDataResult<>(new GenreResponse(genre.getId(), genre.getName()), "genre.found");
    }

    @Override
    @Transactional(readOnly = true)
    public PagedDataResult<GenreResponse> list(String q, Pageable pageable) {
        Page<Genre> page = (q == null || q.isBlank())
                ? genreRepository.findAll(pageable)
                : genreRepository.findAll((root, query, cb) ->
                cb.like(cb.lower(root.get("name")), "%" + q.toLowerCase() + "%"), pageable);

        var items = page.getContent().stream()
                .map(g -> new GenreResponse(g.getId(), g.getName()))
                .collect(Collectors.toList());

        return new PagedDataResult<>(
                items,
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                "genre.list.ok"
        );
    }

    @Override
    public DataResult<GenreResponse> update(Long id, GenreRequest request) {
        var optional = genreRepository.findById(id);
        if (optional.isEmpty()) {
            return new ErrorDataResult<GenreResponse>(null, "genre.notfound");
        }
        Genre genre = optional.get();
        if (request.getName() != null && !request.getName().isBlank()) {
            genre.setName(request.getName().trim());
        }
        Genre saved = genreRepository.save(genre);
        return new SuccessDataResult<>(new GenreResponse(saved.getId(), saved.getName()), "genre.updated");
    }

    @Override
    public Result delete(Long id) {
        if (!genreRepository.existsById(id)) {
            return new ErrorResult("genre.notfound");
        }
        genreRepository.deleteById(id);
        return new SuccessResult("genre.deleted");
    }
}
