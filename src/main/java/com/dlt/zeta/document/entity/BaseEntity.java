package com.dlt.zeta.document.entity;

import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;

import java.util.UUID;

@MappedSuperclass
public class BaseEntity {
    @Id
    private UUID id = UUID.randomUUID();

    public BaseEntity() {
    }

    public UUID getId() {
        return this.id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}