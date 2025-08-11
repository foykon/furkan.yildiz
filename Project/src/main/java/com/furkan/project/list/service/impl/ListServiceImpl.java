package com.furkan.project.list.service.impl;

import com.furkan.project.common.result.*;
import com.furkan.project.list.dto.request.AddListItemRequest;
import com.furkan.project.list.dto.request.ListFilterRequest;
import com.furkan.project.list.dto.request.ReorderRequest;
import com.furkan.project.list.dto.response.ListItemResponse;
import com.furkan.project.list.entity.ListType;
import com.furkan.project.list.entity.UserListItem;
import com.furkan.project.list.repository.*;
import com.furkan.project.list.service.ListService;
import com.furkan.project.movie.repository.MovieRepository;
import com.furkan.project.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;
import com.furkan.project.list.validation.ListValidator;

@Service
@RequiredArgsConstructor
@Transactional
public class ListServiceImpl implements ListService {

    private final UserRepository userRepository;
    private final MovieRepository movieRepository;
    private final UserListItemRepository listRepository;
    private final ListValidator validator;

    @Override
    public DataResult<ListItemResponse> addToList(Long userId, AddListItemRequest req) {
        var v = validator.validateAdd(userId, req);
        if (!v.isSuccess()) return new ErrorDataResult<>(null, v.getMessage());

        if (!Boolean.TRUE.equals(v.getData())) {
            UserListItem li = new UserListItem();
            li.setUser(userRepository.getReferenceById(userId));              // proxy, select yok
            li.setMovie(movieRepository.getReferenceById(req.getMovieId()));  // proxy
            li.setType(req.getType());
            li.setOrderIndex(req.getOrderIndex());
            listRepository.save(li);
        }

        return listRepository.findOneItem(userId, req.getMovieId(), req.getType())
                .<DataResult<ListItemResponse>>map(i -> new SuccessDataResult<>(i, "list.added"))
                .orElseGet(() -> new ErrorDataResult<>(null, "list.added"));
    }

    @Override
    public Result removeFromList(Long userId, Long movieId, ListType type) {
        var v = validator.validateRemove(userId, movieId, type);
        if (!v.isSuccess()) return v;
        listRepository.softDeleteOne(userId, movieId, type); // idempotent
        return new SuccessResult("list.removed");
    }

    @Transactional(readOnly = true)
    @Override
    public PagedDataResult<ListItemResponse> getList(Long userId, ListType type, ListFilterRequest filter, Pageable pageable) {
        var page = listRepository.findList(userId, type, filter != null ? filter.getQ() : null, pageable);
        return new PagedDataResult<>(
                page.getContent(), page.getNumber(), page.getSize(),
                page.getTotalElements(), page.getTotalPages(), true, "ok"
        );
    }

    @Override
    public Result reorder(Long userId, ReorderRequest request) {
        var v = validator.validateReorder(userId, request);
        if (!v.isSuccess()) return v;

        var items = listRepository.findAll((root, cq, cb) -> cb.equal(root.get("user").get("id"), userId));
        var target = request.getItems().stream()
                .collect(Collectors.toMap(
                        it -> it.getMovieId()+"#"+it.getType(),
                        ReorderRequest.ListReorderItem::getOrderIndex,
                        (a,b)->b,
                        LinkedHashMap::new));
        items.forEach(li -> {
            String key = li.getMovie().getId()+"#"+li.getType();
            if (target.containsKey(key)) li.setOrderIndex(target.get(key));
        });
        return new SuccessResult("reordered");
    }

    @Override
    public Result clear(Long userId, ListType type) {
        listRepository.softDeleteAll(userId, type);
        return new SuccessResult("cleared");
    }

    @Transactional(readOnly = true)
    @Override
    public DataResult<Boolean> contains(Long userId, Long movieId, ListType type) {
        boolean exists = listRepository.existsByUserIdAndMovieIdAndTypeAndDeletedFalse(userId, movieId, type);
        return new SuccessDataResult<>(exists, "ok");
    }
}
