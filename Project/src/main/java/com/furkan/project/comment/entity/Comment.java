package com.furkan.project.comment.entity;

import com.furkan.project.common.entity.SoftDeletableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "movie_comments",
        indexes = {
                @Index(name="ix_comment_movie", columnList="movie_id"),
                @Index(name="ix_comment_user",  columnList="user_id"),
                @Index(name="ix_comment_created", columnList="created_at")
        })
@Getter
@Setter
public class Comment extends SoftDeletableEntity {


    @Column(name="movie_id", nullable=false) private Long movieId;
    @Column(name="user_id",  nullable=false) private Long userId;

    @Column(nullable=false, length=1000) private String content;


}
