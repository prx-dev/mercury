package com.prx.mercury.api.v1.service;

import com.prx.mercury.api.v1.to.VerificationCodeRequest;
import com.prx.mercury.api.v1.to.VerificationCodeTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public interface VerificationCodeService {

    default VerificationCodeTO create(VerificationCodeTO verificationCodeTO) {
        throw new UnsupportedOperationException(HttpStatus.NOT_IMPLEMENTED.getReasonPhrase());
    }

    default ResponseEntity<Void> confirmCode(VerificationCodeRequest verificationCodeRequest) {
        throw new UnsupportedOperationException(HttpStatus.NOT_IMPLEMENTED.getReasonPhrase());
    }
}
