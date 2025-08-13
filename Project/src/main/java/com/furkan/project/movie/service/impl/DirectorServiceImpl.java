package com.furkan.project.movie.service.impl;

import com.furkan.project.common.logging.LogExecution;
import com.furkan.project.common.result.*;
import com.furkan.project.movie.dto.director.DirectorRequest;
import com.furkan.project.movie.dto.director.DirectorResponse;
import com.furkan.project.movie.entity.Director;
import com.furkan.project.movie.repository.DirectorRepository;
import com.furkan.project.movie.service.DirectorService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@LogExecution

public class DirectorServiceImpl implements DirectorService {

    private final DirectorRepository directorRepository;

    @Override
    public DataResult<DirectorResponse> create(DirectorRequest request) {
        if (request.getName() == null || request.getName().isBlank()) {
            return new ErrorDataResult<>(null, "director.name.required");
        }
        Director saved = directorRepository.save(Director.builder()
                .name(request.getName().trim())
                .build());

        return new SuccessDataResult<>(new DirectorResponse(saved.getId(), saved.getName(), saved.getNationality()), "director.created");
    }

    @Override
    @Transactional(readOnly = true)
    public DataResult<DirectorResponse> get(Long id) {
        var optional = directorRepository.findById(id);
        if (optional.isEmpty()) {
            return new ErrorDataResult<>(null, "director.notfound");
        }
        Director director = optional.get();
        return new SuccessDataResult<>(new DirectorResponse(director.getId(), director.getName(), director.getNationality()), "director.found");
    }

    @Override
    @Transactional(readOnly = true)
    public PagedDataResult<DirectorResponse> list(String q, Pageable pageable) {
        Page<Director> page = (q == null || q.isBlank())
                ? directorRepository.findAll(pageable)
                : directorRepository.findAll((root, query, cb) ->
                cb.like(cb.lower(root.get("name")), "%" + q.toLowerCase() + "%"), pageable);

        var items = page.getContent().stream()
                .map(d -> new DirectorResponse(d.getId(), d.getName(), d.getNationality()))
                .collect(Collectors.toList());

        return new PagedDataResult<>(
                items,
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                "director.list.ok"
        );
    }

    @Override
    public DataResult<DirectorResponse> update(Long id, DirectorRequest request) {
        var optional = directorRepository.findById(id);
        if (optional.isEmpty()) {
            return new ErrorDataResult<DirectorResponse>(null, "director.notfound");
        }
        Director director = optional.get();
        if (request.getName() != null && !request.getName().isBlank()) {
            director.setName(request.getName().trim());
        }
        Director saved = directorRepository.save(director);
        return new SuccessDataResult<>(new DirectorResponse(saved.getId(), saved.getName(), saved.getNationality()), "director.updated");
    }

    @Override
    public Result delete(Long id) {
        if (!directorRepository.existsById(id)) {
            return new ErrorResult("director.notfound");
        }
        directorRepository.deleteById(id);
        return new SuccessResult("director.deleted");
    }
}

