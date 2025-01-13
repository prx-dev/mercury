package com.prx.mercury.jpa.repository;

import com.prx.mercury.jpa.entity.TemplateUsageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface TemplateUsageEntityRepository extends JpaRepository<TemplateUsageEntity, UUID>, JpaSpecificationExecutor<TemplateUsageEntity> {
}
