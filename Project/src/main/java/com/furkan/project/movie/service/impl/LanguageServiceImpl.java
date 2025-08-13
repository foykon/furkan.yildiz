package com.furkan.project.movie.service.impl;

import com.furkan.project.common.logging.LogExecution;
import com.furkan.project.common.result.*;
import com.furkan.project.movie.dto.language.LanguageRequest;
import com.furkan.project.movie.dto.language.LanguageResponse;
import com.furkan.project.movie.entity.Language;
import com.furkan.project.movie.repository.LanguageRepository;
import com.furkan.project.movie.service.LanguageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@LogExecution

public class LanguageServiceImpl implements LanguageService {

    private final LanguageRepository languageRepository;

    @Override
    public DataResult<LanguageResponse> create(LanguageRequest request) {
        if (request.getName() == null || request.getName().isBlank()) {
            return new ErrorDataResult<LanguageResponse>(null, "language.name.required");
        }
        if (request.getIsoCode() == null || request.getIsoCode().isBlank()) {
            return new ErrorDataResult<LanguageResponse>(null, "language.iso.required");
        }
        Language saved = languageRepository.save(Language.builder()
                .name(request.getName().trim())
                .isoCode(request.getIsoCode().trim())
                .build());

        return new SuccessDataResult<>(new LanguageResponse(saved.getId(), saved.getName(), saved.getIsoCode()),
                "language.created");
    }

    @Override
    @Transactional(readOnly = true)
    public DataResult<LanguageResponse> get(Long id) {
        var optional = languageRepository.findById(id);
        if (optional.isEmpty()) {
            return new ErrorDataResult<LanguageResponse>(null, "language.notfound");
        }
        Language language = optional.get();
        return new SuccessDataResult<>(new LanguageResponse(language.getId(), language.getName(), language.getIsoCode()),
                "language.found");
    }

    @Override
    @Transactional(readOnly = true)
    public PagedDataResult<LanguageResponse> list(String q, Pageable pageable) {
        Page<Language> page = (q == null || q.isBlank())
                ? languageRepository.findAll(pageable)
                : languageRepository.findAll((root, query, cb) ->
                cb.like(cb.lower(root.get("name")), "%" + q.toLowerCase() + "%"), pageable);

        var items = page.getContent().stream()
                .map(l -> new LanguageResponse(l.getId(), l.getName(), l.getIsoCode()))
                .collect(Collectors.toList());

        return new PagedDataResult<>(
                items,
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                "language.list.ok"
        );
    }

    @Override
    public DataResult<LanguageResponse> update(Long id, LanguageRequest request) {
        var optional = languageRepository.findById(id);
        if (optional.isEmpty()) {
            return new ErrorDataResult<LanguageResponse>(null, "language.notfound");
        }
        Language language = optional.get();
        if (request.getName() != null && !request.getName().isBlank()) {
            language.setName(request.getName().trim());
        }
        if (request.getIsoCode() != null && !request.getIsoCode().isBlank()) {
            language.setIsoCode(request.getIsoCode().trim());
        }
        Language saved = languageRepository.save(language);
        return new SuccessDataResult<>(new LanguageResponse(saved.getId(), saved.getName(), saved.getIsoCode()),
                "language.updated");
    }

    @Override
    public Result delete(Long id) {
        if (!languageRepository.existsById(id)) {
            return new ErrorResult("language.notfound");
        }
        languageRepository.deleteById(id);
        return new SuccessResult("language.deleted");
    }
}
