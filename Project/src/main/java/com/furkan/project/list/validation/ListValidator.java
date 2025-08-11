package com.furkan.project.list.validation;

import com.furkan.project.common.result.*;
import com.furkan.project.common.service.MessageService;
import com.furkan.project.list.dto.request.AddListItemRequest;
import com.furkan.project.list.dto.request.ReorderRequest;
import com.furkan.project.list.entity.ListType;
import com.furkan.project.list.repository.UserListItemRepository;
import com.furkan.project.movie.api.MovieReadService;
import com.furkan.project.movie.repository.MovieRepository;
import com.furkan.project.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
@RequiredArgsConstructor
public class ListValidator {

    private final UserRepository userRepository;
    private final MovieRepository movieRepository;
    private final UserListItemRepository listRepository;

    public DataResult<Boolean> validateAdd(Long userId, AddListItemRequest req) {
        if (userId == null) return new ErrorDataResult<>(null, "user.id.required");
        if (req == null) return new ErrorDataResult<>(null, "request.required");
        if (req.getMovieId() == null) return new ErrorDataResult<>(null, "movie.id.required");
        if (req.getType() == null) return new ErrorDataResult<>(null, "list.type.required");

        if (!userRepository.existsById(userId)) return new ErrorDataResult<>(null, "user.notfound");
        if (!movieRepository.existsById(req.getMovieId())) return new ErrorDataResult<>(null, "movie.notfound");

        boolean already = listRepository.existsByUserIdAndMovieIdAndTypeAndDeletedFalse(userId, req.getMovieId(), req.getType());
        return new SuccessDataResult<>(already, "ok");
    }

    public Result validateRemove(Long userId, Long movieId, ListType type) {
        if (userId == null) return new ErrorResult("user.id.required");
        if (movieId == null) return new ErrorResult("movie.id.required");
        if (type == null) return new ErrorResult("list.type.required");
        if (!userRepository.existsById(userId)) return new ErrorResult("user.notfound");
        return new SuccessResult("ok");
    }

    public Result validateReorder(Long userId, ReorderRequest request) {
        if (userId == null) return new ErrorResult("user.id.required");
        if (request == null || request.getItems() == null || request.getItems().isEmpty())
            return new ErrorResult("reorder.items.required");
        boolean invalid = request.getItems().stream().anyMatch(it -> it.getMovieId()==null || it.getType()==null || it.getOrderIndex()==null || it.getOrderIndex()<0);
        if (invalid) return new ErrorResult("reorder.items.invalid");
        if (!userRepository.existsById(userId)) return new ErrorResult("user.notfound");
        return new SuccessResult("ok");
    }
}
