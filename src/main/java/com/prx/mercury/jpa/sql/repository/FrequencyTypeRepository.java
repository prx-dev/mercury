package com.prx.mercury.jpa.sql.repository;

import com.prx.mercury.jpa.sql.entity.FrequencyTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

/// FrequencyRepository.
public interface FrequencyTypeRepository extends JpaRepository<FrequencyTypeEntity, UUID> {
}
