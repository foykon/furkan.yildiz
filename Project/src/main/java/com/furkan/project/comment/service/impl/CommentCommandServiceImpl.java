package com.furkan.project.comment.service.impl;

import com.furkan.project.comment.dto.request.CommentRequest;
import com.furkan.project.comment.dto.response.CommentResponse;
import com.furkan.project.comment.entity.Comment;
import com.furkan.project.comment.mapper.CommentMapper;
import com.furkan.project.comment.repository.CommentRepository;
import com.furkan.project.comment.service.CommentCommandService;
import com.furkan.project.common.security.CurrentUserHelper;
import com.furkan.project.common.service.MessageService;
import com.furkan.project.movie.api.MovieApiService;
import com.furkan.project.user.api.UserApiService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentCommandServiceImpl implements CommentCommandService {

    private final CommentRepository commentRepository;
    private final MovieApiService movieApi;
    private final UserApiService userApi;
    private final CommentMapper mapper;
    private final CurrentUserHelper current;private final MessageService messages;

    @Override
    public CommentResponse create(Long movieId, CommentRequest request) {
        if (!movieApi.existsById(movieId)) throw new EntityNotFoundException(messages.get("movie.notFound"));

        Long userId = current.getIdOrNull();
        if (userId == null) throw new SecurityException(messages.get("auth.required"));

        if (!userApi.existsById(userId)) throw new EntityNotFoundException(messages.get("user.notFound"));

        Comment c = new Comment();
        c.setMovieId(movieId);
        c.setUserId(userId);
        c.setContent(request.getContent());
        commentRepository.save(c);

        String username = userApi.getUserNameById(userId);
        return mapper.toResponse(c, username, userId);
    }

    @Override
    public CommentResponse update(Long movieId, Long commentId, CommentRequest request) {
        var c = commentRepository.findByIdAndMovieIdAndDeletedFalse(commentId, movieId)
                .orElseThrow(() -> new EntityNotFoundException(messages.get("comment.notFound")));

        Long userId = current.getIdOrNull();
        if (userId == null) throw new SecurityException(messages.get("auth.required"));

        boolean isOwner = userId.equals(c.getUserId());
        boolean isAdmin = current.hasRole("ROLE_ADMIN");
        if (!isOwner && !isAdmin) throw new SecurityException(messages.get("forbidden"));

        c.setContent(request.getContent());
        String username = userApi.getUserNameById(c.getUserId());
        return mapper.toResponse(c, username, userId);
    }

    @Override
    public void delete(Long movieId, Long commentId) {
        var c = commentRepository.findByIdAndMovieIdAndDeletedFalse(commentId, movieId)
                .orElseThrow(() -> new EntityNotFoundException(messages.get("comment.notFound")));

        Long userId = current.getIdOrNull();
        if (userId == null) throw new SecurityException(messages.get("auth.required"));

        boolean isOwner = userId.equals(c.getUserId());
        boolean isAdmin = current.hasRole("ROLE_ADMIN");
        if (!isOwner && !isAdmin) throw new SecurityException(messages.get("forbidden"));

        c.setDeleted(true);
    }
}

