package com.furkan.project.comment.service.impl;

import com.furkan.project.comment.dto.response.CommentResponse;
import com.furkan.project.comment.mapper.CommentMapper;
import com.furkan.project.comment.repository.CommentRepository;
import com.furkan.project.comment.service.CommentQueryService;
import com.furkan.project.common.security.CurrentUserHelper;
import com.furkan.project.movie.api.MovieApiService;
import com.furkan.project.user.api.UserApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentQueryServiceImpl implements CommentQueryService {

    private final CommentRepository commentRepository;
    private final MovieApiService movieApi;
    private final UserApiService userApi;
    private final CommentMapper mapper;
    private final CurrentUserHelper current;

    @Override
    public Page<CommentResponse> listByMovie(Long movieId, Pageable pageable) {
        if (!movieApi.existsById(movieId)) {
            throw new jakarta.persistence.EntityNotFoundException("movie.notFound");
        }
        Long me = current.getIdOrNull();
        return commentRepository.findByMovieIdAndDeletedFalse(movieId, pageable)
                .map(c -> mapper.toResponse(c, userApi.getUserNameById(c.getUserId()), me));
    }
}
