package com.prx.mercury.api.v1.service;

import com.prx.mercury.api.v1.to.VerificationCodeTO;
import org.springframework.http.HttpStatus;

public interface VerificationCodeService {

    default VerificationCodeTO create(VerificationCodeTO verificationCodeTO) {
        throw new UnsupportedOperationException(HttpStatus.NOT_IMPLEMENTED.getReasonPhrase());
    }
}
