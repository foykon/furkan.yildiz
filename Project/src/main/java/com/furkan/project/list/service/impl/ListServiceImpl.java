package com.furkan.project.list.service.impl;

import com.furkan.project.common.result.*;
import com.furkan.project.list.dto.request.AddListItemRequest;
import com.furkan.project.list.dto.request.ListFilterRequest;
import com.furkan.project.list.dto.request.ReorderRequest;
import com.furkan.project.list.dto.response.ListItemResponse;
import com.furkan.project.list.entity.ListType;
import com.furkan.project.list.entity.UserListItem;
import com.furkan.project.list.repository.UserListItemRepository;
import com.furkan.project.list.service.ListService;
import com.furkan.project.list.validation.ListValidator;
import com.furkan.project.movie.api.MovieApiService;
import com.furkan.project.movie.api.MovieSummary;
import com.furkan.project.user.api.UserApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ListServiceImpl implements ListService {

    private final UserListItemRepository listRepository;
    private final ListValidator validator;
    private final UserApiService userApi;     // şu an sadece validator kullanıyor ama ileride işine yarar
    private final MovieApiService movieApi;

    // ============== ADD ==============
    @Override
    public DataResult<ListItemResponse> addToList(Long userId, AddListItemRequest req) {
        var v = validator.validateAdd(userId, req);
        if (!v.isSuccess()) return new ErrorDataResult<>(null, v.getMessage());

        if (!Boolean.TRUE.equals(v.getData())) { // zaten yoksa ekle
            UserListItem li = new UserListItem();
            li.setUserId(userId);
            li.setMovieId(req.getMovieId());
            li.setType(req.getType());
            li.setOrderIndex(req.getOrderIndex());
            listRepository.save(li);
        }

        var entityOpt = listRepository.findByUserIdAndMovieIdAndTypeAndDeletedFalse(userId, req.getMovieId(), req.getType());
        if (entityOpt.isEmpty()) return new ErrorDataResult<>(null, "list.added"); // pratikte düşmez

        MovieSummary ms = movieApi.getSummary(req.getMovieId());
        return new SuccessDataResult<>(toResponse(entityOpt.get(), ms), "list.added");
    }

    // ============== REMOVE (idempotent) ==============
    @Override
    public Result removeFromList(Long userId, Long movieId, ListType type) {
        var v = validator.validateRemove(userId, movieId, type);
        if (!v.isSuccess()) return v;
        listRepository.softDeleteOne(userId, movieId, type); // idempotent
        return new SuccessResult("list.removed");
    }

    // ============== GET LIST (q destekli) ==============
    @Transactional(readOnly = true)
    @Override
    public PagedDataResult<ListItemResponse> getList(Long userId, ListType type, ListFilterRequest filter, Pageable pageable) {
        if (userId == null) {
            return new PagedDataResult<>(List.of(), 0, pageable.getPageSize(), 0, 0, false, "user.id.required");
        }
        if (type == null) {
            return new PagedDataResult<>(List.of(), 0, pageable.getPageSize(), 0, 0, false, "list.type.required");
        }

        String q = (filter != null && filter.getQ() != null) ? filter.getQ().trim() : null;
        boolean hasQuery = (q != null && !q.isBlank());

        if (!hasQuery) {
            Page<UserListItem> page = listRepository.findByUserIdAndTypeAndDeletedFalse(userId, type, pageable);

            Map<Long, MovieSummary> summaries = movieApi.getSummaries(
                    page.getContent().stream().map(UserListItem::getMovieId).collect(Collectors.toSet())
            );

            var items = page.getContent().stream()
                    .map(li -> toResponse(li, summaries.get(li.getMovieId())))
                    .collect(Collectors.toList());

            return new PagedDataResult<>(items,
                    page.getNumber(), page.getSize(), page.getTotalElements(), page.getTotalPages(),
                    true, "ok");
        } else {
            // 1) Kullanıcının bu listedeki tüm film id’leri
            List<Long> allIds = listRepository.findMovieIdsByUserAndType(userId, type);
            // 2) Başlıkta q geçen id’ler
            Set<Long> filtered = movieApi.filterIdsByTitleWithin(allIds, q);

            // 3) orderIndex/createdAt sırasına göre diz, sonra filtre uygula
            List<Long> ordered = applyOrderIndex(userId, type);
            List<Long> filteredOrdered = ordered.stream().filter(filtered::contains).toList();

            // 4) Manuel pagination
            int page = pageable.getPageNumber();
            int size = pageable.getPageSize();
            int total = filteredOrdered.size();
            int from = Math.min(page * size, total);
            int to = Math.min(from + size, total);
            List<Long> pageIds = (from < to) ? filteredOrdered.subList(from, to) : List.of();

            Map<Long, MovieSummary> summaries = movieApi.getSummaries(new HashSet<>(pageIds));

            // entity meta (createdAt/orderIndex) için map
            Map<Long, UserListItem> liByMovieId = listRepository
                    .findByUserIdAndTypeAndDeletedFalse(userId, type, Pageable.unpaged())
                    .getContent().stream()
                    .collect(Collectors.toMap(UserListItem::getMovieId, x -> x, (a,b)->a));

            var items = pageIds.stream()
                    .map(id -> toResponse(liByMovieId.get(id), summaries.get(id)))
                    .toList();

            int totalPages = (int) Math.ceil(total / (double) size);
            return new PagedDataResult<>(items, page, size, total, totalPages, true, "ok");
        }
    }

    // ============== REORDER (opsiyonel) ==============
    @Override
    public Result reorder(Long userId, ReorderRequest request) {
        var v = validator.validateReorder(userId, request);
        if (!v.isSuccess()) return v;

        var items = listRepository.findAll((root, cq, cb) -> cb.and(
                cb.equal(root.get("userId"), userId),
                cb.isFalse(root.get("deleted"))
        ));

        Map<String, Integer> target = request.getItems().stream()
                .collect(Collectors.toMap(
                        it -> it.getMovieId() + "#" + it.getType(),
                        ReorderRequest.ListReorderItem::getOrderIndex,
                        (a,b)->b,
                        LinkedHashMap::new
                ));

        items.forEach(li -> {
            String key = li.getMovieId() + "#" + li.getType();
            if (target.containsKey(key)) li.setOrderIndex(target.get(key));
        });

        return new SuccessResult("reordered");
    }

    // ============== CLEAR (opsiyonel) ==============
    @Override
    public Result clear(Long userId, ListType type) {
        if (userId == null || type == null) return new ErrorResult("bad.request");
        listRepository.softDeleteAll(userId, type);
        return new SuccessResult("cleared");
    }

    // ============== CONTAINS ==============
    @Transactional(readOnly = true)
    @Override
    public DataResult<Boolean> contains(Long userId, Long movieId, ListType type) {
        boolean exists = listRepository.existsByUserIdAndMovieIdAndTypeAndDeletedFalse(userId, movieId, type);
        return new SuccessDataResult<>(exists, "ok");
    }

    // -------- helpers --------

    private ListItemResponse toResponse(UserListItem li, MovieSummary ms) {
        if (li == null) return null;
        String title = (ms != null) ? ms.title() : null;
        String poster = (ms != null) ? ms.posterUrl() : null;
        Integer year = (ms != null) ? ms.releaseYear() : null;

        return new ListItemResponse(
                li.getMovieId(),
                title,
                poster,
                year,
                li.getType(),
                Optional.ofNullable(li.getCreatedAt()).orElse(Instant.now()),
                li.getOrderIndex()
        );
    }

    // orderIndex (null -> sona), sonra createdAt desc
    private List<Long> applyOrderIndex(Long userId, ListType type) {
        var items = listRepository.findAll((root, cq, cb) -> cb.and(
                cb.equal(root.get("userId"), userId),
                cb.equal(root.get("type"), type),
                cb.isFalse(root.get("deleted"))
        ));
        return items.stream()
                .sorted(Comparator
                        .comparing((UserListItem x) -> Optional.ofNullable(x.getOrderIndex()).orElse(Integer.MAX_VALUE))
                        .thenComparing(UserListItem::getCreatedAt, Comparator.nullsLast(Comparator.reverseOrder())))
                .map(UserListItem::getMovieId)
                .toList();
    }
}
