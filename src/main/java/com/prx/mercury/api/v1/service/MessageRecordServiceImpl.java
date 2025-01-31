package com.prx.mercury.api.v1.service;

import com.prx.mercury.api.v1.to.MessageRecordTO;
import com.prx.mercury.jpa.sql.repository.MessageRecordRepository;
import com.prx.mercury.mapper.MessageRecordMapper;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class MessageRecordServiceImpl implements MessageRecordService {

    private final MessageRecordRepository messageRecordRepository;
    private final MessageRecordMapper messageRecordMapper;

    public MessageRecordServiceImpl(MessageRecordRepository messageRecordRepository, MessageRecordMapper messageRecordMapper) {
        this.messageRecordRepository = messageRecordRepository;
        this.messageRecordMapper = messageRecordMapper;
    }

    @Override
    public MessageRecordTO create(MessageRecordTO messageRecordTO) {
        if (Objects.isNull(messageRecordTO)) {
            throw new IllegalArgumentException("Message record is empty");
        }
        final var messageRecordEntity = messageRecordMapper.toMessageRecordEntity(messageRecordTO);
        final var result = messageRecordRepository.save(messageRecordEntity);

        return messageRecordMapper.toMessageRecordTO(result);
    }
}
