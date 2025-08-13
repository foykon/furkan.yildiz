package com.furkan.project.movie.service.impl;

import com.furkan.project.common.logging.LogExecution;
import com.furkan.project.common.result.*;
import com.furkan.project.movie.dto.country.CountryRequest;
import com.furkan.project.movie.dto.country.CountryResponse;
import com.furkan.project.movie.entity.Country;
import com.furkan.project.movie.repository.CountryRepository;
import com.furkan.project.movie.service.CountryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@LogExecution

public class CountryServiceImpl implements CountryService {

    private final CountryRepository countryRepository;

    @Override
    public DataResult<CountryResponse> create(CountryRequest request) {
        if (request.getName() == null || request.getName().isBlank()) {
            return new ErrorDataResult<CountryResponse>(null, "country.name.required");
        }
        Country saved = countryRepository.save(Country.builder()
                .name(request.getName().trim())
                .build());

        return new SuccessDataResult<>(new CountryResponse(saved.getId(), saved.getName()), "country.created");
    }

    @Override
    @Transactional(readOnly = true)
    public DataResult<CountryResponse> get(Long id) {
        var optional = countryRepository.findById(id);
        if (optional.isEmpty()) {
            return new ErrorDataResult<CountryResponse>(null, "country.notfound");
        }
        Country country = optional.get();
        return new SuccessDataResult<>(new CountryResponse(country.getId(), country.getName()), "country.found");
    }

    @Override
    @Transactional(readOnly = true)
    public PagedDataResult<CountryResponse> list(String q, Pageable pageable) {
        Page<Country> page = (q == null || q.isBlank())
                ? countryRepository.findAll(pageable)
                : countryRepository.findAll((root, query, cb) ->
                cb.like(cb.lower(root.get("name")), "%" + q.toLowerCase() + "%"), pageable);

        var items = page.getContent().stream()
                .map(c -> new CountryResponse(c.getId(), c.getName()))
                .collect(Collectors.toList());

        return new PagedDataResult<>(
                items,
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                "country.list.ok"
        );
    }

    @Override
    public DataResult<CountryResponse> update(Long id, CountryRequest request) {
        var optional = countryRepository.findById(id);
        if (optional.isEmpty()) {
            return new ErrorDataResult<CountryResponse>(null, "country.notfound");
        }
        Country country = optional.get();
        if (request.getName() != null && !request.getName().isBlank()) {
            country.setName(request.getName().trim());
        }
        Country saved = countryRepository.save(country);
        return new SuccessDataResult<>(new CountryResponse(saved.getId(), saved.getName()), "country.updated");
    }

    @Override
    public Result delete(Long id) {
        if (!countryRepository.existsById(id)) {
            return new ErrorResult("country.notfound");
        }
        countryRepository.deleteById(id);
        return new SuccessResult("country.deleted");
    }
}
