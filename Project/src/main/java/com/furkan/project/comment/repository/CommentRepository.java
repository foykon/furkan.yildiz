package com.furkan.project.comment.repository;

import com.furkan.project.comment.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Page<Comment> findByMovieIdAndDeletedFalse(Long movieId, Pageable pageable);
    Optional<Comment> findByIdAndMovieIdAndDeletedFalse(Long id, Long movieId);
}
