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

            /// Repository interface for managing [VerificationCodeEntity] objects.
            /// Provides methods to perform CRUD operations on verification codes in the database.
            /// Extends JpaRepository for standard operations and JpaSpecificationExecutor for dynamic query capabilities.
            public interface VerificationCodeRepository extends JpaRepository<VerificationCodeEntity, UUID>, JpaSpecificationExecutor<VerificationCodeEntity> {

                /// Finds verification codes that match specific criteria:
                /// - Associated with a specific user
                /// - Associated with a specific application
                /// - Not yet expired (expiration date is after the specified date)
                /// - Having the specified verification status
                ///
                /// @param userId The ID of the user associated with the verification codes
                /// @param applicationId The ID of the application associated with the verification codes
                /// @param expiresAtAfter The date time to compare against expiration date (codes expiring after this date will be returned)
                /// @param isVerified The verification status to filter by
                /// @return A list of verification code entities matching the criteria
                @Query("SELECT vce FROM VerificationCodeEntity vce WHERE vce.userEntity.id = :userId AND vce.applicationEntity.id = :applicationId AND vce.expiresAt > :expiresAtAfter AND vce.isVerified = :isVerified")
                List<VerificationCodeEntity> findByUserIdAndApplicationIdAndExpiresAtBeforeAndIsVerified(
                        @Param("userId") UUID userId,
                        @Param("applicationId") UUID applicationId,
                        @Param("expiresAtAfter") LocalDateTime expiresAtAfter,
                        @Param("isVerified") @NotNull Boolean isVerified);
            }
