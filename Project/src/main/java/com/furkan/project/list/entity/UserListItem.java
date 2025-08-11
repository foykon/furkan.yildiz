package com.furkan.project.list.entity;

import com.furkan.project.common.entity.BaseEntity;
import com.furkan.project.common.entity.SoftDeletableEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
@Entity
@Table(
        name = "user_list_item",
        uniqueConstraints = @UniqueConstraint(name = "uk_user_movie_type", columnNames = {"user_id","movie_id","type"}),
        indexes = {
                @Index(name = "idx_user_type", columnList = "user_id,type"),
                @Index(name = "idx_movie", columnList = "movie_id")
        }
)
public class UserListItem extends SoftDeletableEntity {

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "movie_id", nullable = false)
    private Long movieId;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, length = 16)
    private ListType type;

    @Column(name = "order_index")
    private Integer orderIndex;
}
