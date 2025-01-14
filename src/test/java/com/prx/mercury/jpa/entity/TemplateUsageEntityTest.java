package com.prx.mercury.jpa.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class TemplateUsageEntityTest {

    @Test
    @DisplayName("Create TemplateUsageEntity with valid data")
    void createTemplateUsageEntityWithValidData() {
        TemplateUsageEntity templateUsageEntity = new TemplateUsageEntity();
        UUID id = UUID.randomUUID();
        templateUsageEntity.setId(id);
        templateUsageEntity.setTemplate(new TemplateEntity());
        templateUsageEntity.setUserId(UUID.randomUUID());
        templateUsageEntity.setApplication(new ApplicationEntity());
        templateUsageEntity.setCreatedAt(LocalDateTime.now());
        templateUsageEntity.setUpdatedAt(LocalDateTime.now());
        templateUsageEntity.setExpiredAt(LocalDateTime.now().plusDays(1));
        templateUsageEntity.setIsActive(true);

        assertEquals(id, templateUsageEntity.getId());
        assertNotNull(templateUsageEntity.getTemplate());
        assertNotNull(templateUsageEntity.getUserId());
        assertNotNull(templateUsageEntity.getApplication());
        assertNotNull(templateUsageEntity.getCreatedAt());
        assertNotNull(templateUsageEntity.getUpdatedAt());
        assertNotNull(templateUsageEntity.getExpiredAt());
        assertTrue(templateUsageEntity.getIsActive());
    }

    @Test
    @DisplayName("Set and get template")
    void setAndGetTemplate() {
        TemplateUsageEntity templateUsageEntity = new TemplateUsageEntity();
        TemplateEntity templateEntity = new TemplateEntity();
        templateUsageEntity.setTemplate(templateEntity);

        assertEquals(templateEntity, templateUsageEntity.getTemplate());
    }

    @Test
    @DisplayName("Set and get user ID")
    void setAndGetUserId() {
        TemplateUsageEntity templateUsageEntity = new TemplateUsageEntity();
        UUID userId = UUID.randomUUID();
        templateUsageEntity.setUserId(userId);

        assertEquals(userId, templateUsageEntity.getUserId());
    }

    @Test
    @DisplayName("Set and get application")
    void setAndGetApplication() {
        TemplateUsageEntity templateUsageEntity = new TemplateUsageEntity();
        ApplicationEntity applicationEntity = new ApplicationEntity();
        templateUsageEntity.setApplication(applicationEntity);

        assertEquals(applicationEntity, templateUsageEntity.getApplication());
    }

    @Test
    @DisplayName("Set and get created at")
    void setAndGetCreatedAt() {
        TemplateUsageEntity templateUsageEntity = new TemplateUsageEntity();
        LocalDateTime now = LocalDateTime.now();
        templateUsageEntity.setCreatedAt(now);

        assertEquals(now, templateUsageEntity.getCreatedAt());
    }

    @Test
    @DisplayName("Set and get updated at")
    void setAndGetUpdatedAt() {
        TemplateUsageEntity templateUsageEntity = new TemplateUsageEntity();
        LocalDateTime now = LocalDateTime.now();
        templateUsageEntity.setUpdatedAt(now);

        assertEquals(now, templateUsageEntity.getUpdatedAt());
    }

    @Test
    @DisplayName("Set and get expired at")
    void setAndGetExpiredAt() {
        TemplateUsageEntity templateUsageEntity = new TemplateUsageEntity();
        LocalDateTime now = LocalDateTime.now().plusDays(1);
        templateUsageEntity.setExpiredAt(now);

        assertEquals(now, templateUsageEntity.getExpiredAt());
    }

    @Test
    @DisplayName("Set and get is active")
    void setAndGetIsActive() {
        TemplateUsageEntity templateUsageEntity = new TemplateUsageEntity();
        templateUsageEntity.setIsActive(false);

        assertFalse(templateUsageEntity.getIsActive());
    }
}
