package com.furkan.project.user.entity;

import com.furkan.project.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "roles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Role extends BaseEntity {

    @Enumerated(EnumType.STRING)
    private ERole name;

    @Column(name = "description", length = 255)
    private String description;

}
