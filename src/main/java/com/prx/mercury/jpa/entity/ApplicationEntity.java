package com.prx.mercury.jpa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.UUID;

@Entity
@Table(name = "application", schema = "general")
public class ApplicationEntity {
    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    /// Create an instance of ApplicationEntity
    public ApplicationEntity() {
        // Default contructor
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

}
