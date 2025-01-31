package com.prx.mercury.jpa.sql.repository;

import com.prx.mercury.jpa.sql.entity.VerificationCodeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface VerificationCodeRepository extends JpaRepository<VerificationCodeEntity, UUID>, JpaSpecificationExecutor<VerificationCodeEntity> {
}
