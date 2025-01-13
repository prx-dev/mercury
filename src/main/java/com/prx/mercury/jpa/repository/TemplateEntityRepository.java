package com.prx.mercury.jpa.repository;

import com.prx.mercury.jpa.entity.TemplateEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface TemplateEntityRepository extends JpaRepository<TemplateEntity, UUID>, JpaSpecificationExecutor<TemplateEntity> {
}
