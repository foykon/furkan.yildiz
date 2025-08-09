package com.furkan.project.common.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@MappedSuperclass
@Getter @Setter
public abstract class SoftDeletableEntity extends AuditableEntity {

    @Column(nullable = false)
    private boolean deleted = false;

    private Instant deletedAt;
}
