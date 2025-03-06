package com.prx.mercury.jpa.sql.repository;

import com.prx.mercury.jpa.sql.entity.VerificationCodeEntity;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface VerificationCodeRepository extends JpaRepository<VerificationCodeEntity, UUID>, JpaSpecificationExecutor<VerificationCodeEntity> {

    @Query("SELECT vce FROM VerificationCodeEntity vce WHERE vce.userEntity.id = :userId AND vce.applicationEntity.id = :applicationId AND vce.expiresAt < :expiresAtAfter AND vce.isVerified = :isVerified")
    List<VerificationCodeEntity> findByUserIdAndApplicationIdAndExpiresAtBeforeAndIsVerified(
            @Param("userId") UUID userId,
            @Param("applicationId") UUID applicationId,
            @Param("expiresAtAfter") LocalDateTime expiresAtAfter,
            @Param("isVerified") @NotNull Boolean isVerified);
}
