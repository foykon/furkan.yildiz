package com.furkan.project.movie.service.impl;

import com.furkan.project.common.logging.LogExecution;
import com.furkan.project.common.result.*;
import com.furkan.project.common.service.MessageService;
import com.furkan.project.movie.dto.actor.ActorRequest;
import com.furkan.project.movie.dto.actor.ActorResponse;
import com.furkan.project.movie.entity.Actor;
import com.furkan.project.movie.repository.ActorRepository;
import com.furkan.project.movie.service.ActorService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@LogExecution

public class ActorServiceImpl implements ActorService {

    private final ActorRepository actorRepository;
    private final MessageService messages;

    @Override
    public DataResult<ActorResponse> create(ActorRequest request) {
        if (request.getName() == null || request.getName().isBlank()) {
            return new ErrorDataResult<>(null, messages.get("actor.name.required"));
        }
        Actor saved = actorRepository.save(Actor.builder()
                .name(request.getName().trim())
                .build());

        return new SuccessDataResult<>(new ActorResponse(saved.getId(), saved.getName(), saved.getNationality()), messages.get("actor.created"));
    }

    @Override
    @Transactional(readOnly = true)
    public DataResult<ActorResponse> get(Long id) {
        var optional = actorRepository.findById(id);
        if (optional.isEmpty()) {
            return new ErrorDataResult<>(null, messages.get("actor.notfound"));
        }
        Actor actor = optional.get();
        return new SuccessDataResult<>(new ActorResponse(actor.getId(), actor.getName(), actor.getNationality()), messages.get("actor.found"));
    }

    @Override
    @Transactional(readOnly = true)
    public PagedDataResult<ActorResponse> list(String q, Pageable pageable) {
        Page<Actor> page = (q == null || q.isBlank())
                ? actorRepository.findAll(pageable)
                : actorRepository.findAll((root, query, cb) ->
                cb.like(cb.lower(root.get("name")), "%" + q.toLowerCase() + "%"), pageable);

        var items = page.getContent().stream()
                .map(a -> new ActorResponse(a.getId(), a.getName(), a.getNationality() ))
                .collect(Collectors.toList());

        return new PagedDataResult<>(
                items,
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                messages.get("actor.list.ok")
        );
    }

    @Override
    public DataResult<ActorResponse> update(Long id, ActorRequest request) {
        var optional = actorRepository.findById(id);
        if (optional.isEmpty()) {
            return new ErrorDataResult<>(null, messages.get("actor.notfound"));
        }
        Actor actor = optional.get();
        if (request.getName() != null && !request.getName().isBlank()) {
            actor.setName(request.getName().trim());
        }
        Actor saved = actorRepository.save(actor);
        return new SuccessDataResult<>(new ActorResponse(saved.getId(), saved.getName(), saved.getNationality()), messages.get("actor.updated"));
    }

    @Override
    public Result delete(Long id) {
        if (!actorRepository.existsById(id)) {
            return new ErrorResult(messages.get("actor.notfound"));
        }
        actorRepository.deleteById(id);
        return new SuccessResult(messages.get("actor.deleted"));
    }
}
