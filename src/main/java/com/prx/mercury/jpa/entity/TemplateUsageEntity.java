package com.prx.mercury.jpa.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "template_usages", schema = "mercury")
public class TemplateUsageEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @ColumnDefault("mercury.uuid_generate_v4()")
    @Column(name = "id", nullable = false)
    private UUID id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "template_id", nullable = false)
    private TemplateEntity templateEntity;

    @NotNull
    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "application_id", nullable = false)
    private ApplicationEntity applicationEntity;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "expired_at")
    private LocalDateTime expiredAt;

    @ColumnDefault("true")
    @Column(name = "is_active")
    private Boolean isActive;


    ///  Create an instance of TemplateUsageEntity
    public TemplateUsageEntity() {
        // Default constructor
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public TemplateEntity getTemplate() {
        return templateEntity;
    }

    public void setTemplate(TemplateEntity templateEntity) {
        this.templateEntity = templateEntity;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public ApplicationEntity getApplication() {
        return applicationEntity;
    }

    public void setApplication(ApplicationEntity applicationEntity) {
        this.applicationEntity = applicationEntity;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public LocalDateTime getExpiredAt() {
        return expiredAt;
    }

    public void setExpiredAt(LocalDateTime expiredAt) {
        this.expiredAt = expiredAt;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

}
