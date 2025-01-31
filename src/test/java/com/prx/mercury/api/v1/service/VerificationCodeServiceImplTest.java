package com.prx.mercury.api.v1.service;

import com.prx.mercury.api.v1.to.VerificationCodeTO;
import com.prx.mercury.jpa.sql.entity.ApplicationEntity;
import com.prx.mercury.jpa.sql.entity.UserEntity;
import com.prx.mercury.jpa.sql.entity.VerificationCodeEntity;
import com.prx.mercury.jpa.sql.repository.VerificationCodeRepository;
import com.prx.mercury.mapper.VerificationCodeMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class VerificationCodeServiceImplTest {

    @Mock
    private VerificationCodeRepository verificationCodeRepository;

    @Mock
    private VerificationCodeMapper verificationCodeMapper;

    @InjectMocks
    private VerificationCodeServiceImpl verificationCodeServiceImpl;

    public VerificationCodeServiceImplTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Create VerificationCodeTO with valid data")
    void createVerificationCodeTOWithValidData() {
        UUID id = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        UUID applicationId = UUID.randomUUID();
        String verificationCode = "123456";
        LocalDateTime now = LocalDateTime.now();
        Boolean isVerified = false;
        Integer attempts = 0;
        Integer maxAttempts = 5;
        String createdBy = "user";
        String updatedBy = "user";
        UUID messageRecordId = UUID.randomUUID();
        VerificationCodeEntity verificationCodeEntity = new VerificationCodeEntity();
        verificationCodeEntity.setId(id);
        verificationCodeEntity.setUser(new UserEntity());
        verificationCodeEntity.setApplication(new ApplicationEntity());
        verificationCodeEntity.setVerificationCode("123456");
        verificationCodeEntity.setCreatedAt(LocalDateTime.now());
        verificationCodeEntity.setExpiresAt(LocalDateTime.now().plusDays(1));
        verificationCodeEntity.setVerifiedAt(LocalDateTime.now());
        verificationCodeEntity.setIsVerified(true);
        verificationCodeEntity.setAttempts(1);
        verificationCodeEntity.setMaxAttempts(3);
        verificationCodeEntity.setCreatedBy("creator");
        verificationCodeEntity.setModifiedBy("modifier");
        verificationCodeEntity.setModifiedAt(LocalDateTime.now());
        VerificationCodeEntity savedEntity = new VerificationCodeEntity();

        VerificationCodeTO verificationCodeTO = new VerificationCodeTO(
                id, userId, applicationId, verificationCode, now, now, now.plusDays(1), null, isVerified, attempts, maxAttempts, createdBy, updatedBy, messageRecordId
        );

        when(verificationCodeMapper.toVerificationCodeEntity(any(VerificationCodeTO.class))).thenReturn(verificationCodeEntity);
        when(verificationCodeRepository.save(any(VerificationCodeEntity.class))).thenReturn(savedEntity);
        when(verificationCodeMapper.toVerificationCodeTO(any(VerificationCodeEntity.class))).thenReturn(verificationCodeTO);

        VerificationCodeTO result = verificationCodeServiceImpl.create(verificationCodeTO);

        assertNotNull(result);
        assertEquals(verificationCodeTO, result);
        verify(verificationCodeMapper).toVerificationCodeEntity(verificationCodeTO);
        verify(verificationCodeRepository).save(verificationCodeEntity);
        verify(verificationCodeMapper).toVerificationCodeTO(savedEntity);
    }

}
