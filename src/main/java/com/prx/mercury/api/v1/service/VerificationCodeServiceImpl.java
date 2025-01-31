package com.prx.mercury.api.v1.service;

import com.prx.mercury.api.v1.to.VerificationCodeTO;
import com.prx.mercury.jpa.sql.repository.VerificationCodeRepository;
import com.prx.mercury.mapper.VerificationCodeMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class VerificationCodeServiceImpl implements VerificationCodeService {

    private static final Logger logger = LoggerFactory.getLogger(VerificationCodeServiceImpl.class);

    private final VerificationCodeRepository verificationCodeRepository;
    private final VerificationCodeMapper verificationCodeMapper;

    public VerificationCodeServiceImpl(VerificationCodeRepository verificationCodeRepository, VerificationCodeMapper verificationCodeMapper) {
        this.verificationCodeRepository = verificationCodeRepository;
        this.verificationCodeMapper = verificationCodeMapper;
    }

    @Override
    public VerificationCodeTO create(VerificationCodeTO verificationCodeTO) {
        logger.debug("Creating verification code: {}", verificationCodeTO);
        return verificationCodeMapper.toVerificationCodeTO(
                verificationCodeRepository.save(verificationCodeMapper
                        .toVerificationCodeEntity(verificationCodeTO)));
    }
}
