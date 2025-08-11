package com.furkan.project.list.entity;

import com.furkan.project.common.entity.AuditableEntity;
import com.furkan.project.common.entity.SoftDeletableEntity;
import com.furkan.project.movie.entity.Movie;
import com.furkan.project.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(
        name = "user_list_item",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_user_movie_type", columnNames = {"user_id", "movie_id", "type"})
        },
        indexes = {
                @Index(name = "idx_user_type", columnList = "user_id,type"),
                @Index(name = "idx_movie", columnList = "movie_id")
        }
)
@SQLDelete(sql = "UPDATE user_list_item SET deleted = true WHERE id = ?")
@Where(clause = "deleted = false")
public class UserListItem extends SoftDeletableEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_list_user"))
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "movie_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_list_movie"))
    private Movie movie;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, length = 16)
    private ListType type;

    @Column(name = "order_index")
    private Integer orderIndex;

}

