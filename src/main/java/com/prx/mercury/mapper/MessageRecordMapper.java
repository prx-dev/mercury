package com.prx.mercury.mapper;

import com.prx.commons.services.config.mapper.MapperAppConfig;
import com.prx.mercury.api.v1.to.EmailContact;
import com.prx.mercury.jpa.nosql.entity.EmailMessageDocument;
import com.prx.mercury.jpa.sql.entity.MessageRecordEntity;
import com.prx.mercury.jpa.sql.entity.MessageStatusTypeEntity;
import com.prx.mercury.jpa.sql.entity.TemplateDefinedEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(
        // Specifies that the mapper should be a Spring bean.
        uses = {EmailMessageDocument.class, MessageRecordEntity.class},
        // Specifies the configuration class to use for this mapper.
        config = MapperAppConfig.class
)
public interface MessageRecordMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "templateDefined", source = "templateDefined")
    @Mapping(target = "sender", source = "emailMessageDocument.from")
    @Mapping(target = "content", source = "emailMessageDocument.body")
    @Mapping(target = "subject", source = "emailMessageDocument.subject")
    @Mapping(target = "createdAt", source = "emailMessageDocument.sendDate")
    @Mapping(target = "updatedAt", source = "emailMessageDocument.sendDate")
    @Mapping(target = "messageStatusType", source = "messageStatusTypeEntity")
    @Mapping(target = "to", expression = "java(listToString(emailMessageDocument.to()))")
    @Mapping(target = "cc", expression = "java(listToString(emailMessageDocument.cc()))")
    MessageRecordEntity toMessageRecordEntity(EmailMessageDocument emailMessageDocument,
                                              MessageStatusTypeEntity messageStatusTypeEntity,
                                              TemplateDefinedEntity templateDefined
    );

    default String listToString(List<EmailContact> list) {
        StringBuilder sb = new StringBuilder();
        if (list != null) {
            list.stream().map(EmailContact::toString).forEach(sb::append);
        }
        return sb.toString();
    }

}
