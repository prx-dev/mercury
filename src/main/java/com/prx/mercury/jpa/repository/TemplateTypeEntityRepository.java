package com.prx.mercury.jpa.repository;

import com.prx.mercury.jpa.entity.TemplateTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface TemplateTypeEntityRepository extends JpaRepository<TemplateTypeEntity, UUID>, JpaSpecificationExecutor<TemplateTypeEntity> {
}
