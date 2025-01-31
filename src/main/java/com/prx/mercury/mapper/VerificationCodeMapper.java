package com.prx.mercury.mapper;

import com.prx.commons.services.config.mapper.MapperAppConfig;
import com.prx.mercury.api.v1.to.VerificationCodeTO;
import com.prx.mercury.jpa.sql.entity.ApplicationEntity;
import com.prx.mercury.jpa.sql.entity.MessageRecordEntity;
import com.prx.mercury.jpa.sql.entity.UserEntity;
import com.prx.mercury.jpa.sql.entity.VerificationCodeEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(
        // Specifies that the mapper should be a Spring bean.
        uses = {VerificationCodeTO.class, VerificationCodeEntity.class},
        // Specifies the configuration class to use for this mapper.
        config = MapperAppConfig.class
)
public interface VerificationCodeMapper {

    @Mapping(target = "modifiedBy", source = "modifiedAt")
    @Mapping(target = "expiresAt", source = "expiredAt")
    @Mapping(target = "user", expression = "java(toUserEntity(verificationCodeTO))")
    @Mapping(target = "application", expression = "java(toApplicationEntity(verificationCodeTO))")
    @Mapping(target = "messageRecord", expression = "java(toMessageRecordEntity(verificationCodeTO))")
    VerificationCodeEntity toVerificationCodeEntity(VerificationCodeTO verificationCodeTO);

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "expiredAt", source = "expiresAt")
    @Mapping(target = "updatedBy", source = "modifiedBy")
    @Mapping(target = "applicationId", source = "application.id")
    @Mapping(target = "messageRecordId", source = "messageRecord.id")
    VerificationCodeTO toVerificationCodeTO(VerificationCodeEntity verificationCodeEntity);

    default ApplicationEntity toApplicationEntity(VerificationCodeTO verificationCodeTO) {
        var applicationEntity = new ApplicationEntity();
        applicationEntity.setId(verificationCodeTO.applicationId());
        return applicationEntity;
    }

    default UserEntity toUserEntity(VerificationCodeTO verificationCodeTO) {
        var userEntity = new UserEntity();
        userEntity.setId(verificationCodeTO.userId());
        return userEntity;
    }

    default MessageRecordEntity toMessageRecordEntity(VerificationCodeTO verificationCodeTO) {
        var messageRecordEntity = new MessageRecordEntity();
        messageRecordEntity.setId(verificationCodeTO.messageRecordId());
        return messageRecordEntity;
    }
}
