package com.prx.mercury.api.v1.service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class VerificationCodeServiceTest {

    private final VerificationCodeService verificationCodeService = new VerificationCodeService() {
    };

    @Test
    void testCreate() {
        UnsupportedOperationException exception = assertThrows(UnsupportedOperationException.class, () -> verificationCodeService.create(null));
        assertEquals(org.springframework.http.HttpStatus.NOT_IMPLEMENTED.getReasonPhrase(), exception.getMessage());
    }

}
