package com.prx.mercury.kafka.consumer;

import com.prx.mercury.jpa.nosql.repository.EmailMessageRepository;
import com.prx.mercury.kafka.to.EmailMessageTO;
import com.prx.mercury.mapper.EmailMessageMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmailMessageConsumerServiceImp implements EmailMessageConsumerService {

    private final Logger logger = LoggerFactory.getLogger(EmailMessageConsumerServiceImp.class);

    private final EmailMessageRepository emaiMessageRepository;
    private final EmailMessageMapper emailMessageMapper;


    ///  Default constructor
    public EmailMessageConsumerServiceImp(EmailMessageRepository emaiMessageRepository, EmailMessageMapper emailMessageMapper) {
        this.emaiMessageRepository = emaiMessageRepository;
        this.emailMessageMapper = emailMessageMapper;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<EmailMessageTO> saveAll(List<EmailMessageTO> emailMessageTOS) {
        return emailMessageTOS;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EmailMessageTO save(EmailMessageTO emailMessageTO) {
        logger.info("Saving Email Message: {}", emailMessageTO);
        // NoSQL save
        var emailMessageDocument = emailMessageMapper.toEmailMessageDocument(emailMessageTO);
        emaiMessageRepository.save(emailMessageDocument);
        // Cache save
        logger.info("Email Message saved on cache: {}", emailMessageDocument);
        return emailMessageTO;
    }
}
