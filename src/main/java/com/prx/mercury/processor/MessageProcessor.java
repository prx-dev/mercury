package com.prx.mercury.processor;

import com.prx.mercury.api.v1.service.*;
import com.prx.mercury.api.v1.to.MessageRecordTO;
import com.prx.mercury.api.v1.to.TemplateDefinedTO;
import com.prx.mercury.api.v1.to.VerificationCodeTO;
import com.prx.mercury.constant.DeliveryStatusType;
import com.prx.mercury.jpa.nosql.entity.EmailMessageDocument;
import com.prx.mercury.mapper.MessageRecordMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Component
public class MessageProcessor {

    private static final Logger logger = LoggerFactory.getLogger(MessageProcessor.class);

    private final EmailService emailService;
    private final MessageRecordService messageRecordService;
    private final TemplateDefinedService templateDefinedService;
    private final MessageRecordMapper messageRecordMapper;
    private final MessageStatusTypeService messageStatusTypeService;
    private final VerificationCodeService verificationCodeService;
    @Value("${prx.verification.code.template.id}")
    private UUID verificationCodeId;

    public MessageProcessor(EmailService emailService, MessageRecordService messageRecordService,
                            TemplateDefinedService templateDefinedService, MessageRecordMapper messageRecordMapper,
                            MessageStatusTypeService messageStatusTypeService, VerificationCodeService verificationCodeService) {
        this.emailService = emailService;
        this.messageRecordService = messageRecordService;
        this.templateDefinedService = templateDefinedService;
        this.messageRecordMapper = messageRecordMapper;
        this.messageStatusTypeService = messageStatusTypeService;
        this.verificationCodeService = verificationCodeService;
    }

    @Transactional
    public void processMessage() {
        // Load the messages with the OPENED status from mongo db
        emailService.findByDeliveryStatus(DeliveryStatusType.OPENED)
                .forEach(emailMessageDocument -> {
                    // Load the template defined for each message
                    var templateDefinedTO = templateDefinedService.find(emailMessageDocument.templateDefinedId());
                    logger.debug("Sending email: {}", emailMessageDocument);
                    // Send the email and update the status
                    emailService.updateEmailStatus(emailService.sendEmail(emailMessageDocument, templateDefinedTO));
                    logger.debug("Email sent: {}", emailMessageDocument);
                });
    }

    @Transactional
    public void updateMessageStatus() {
        // Save the message record
        emailService.findByDeliveryStatus(DeliveryStatusType.SENT)
                .forEach(emailMessageDocument -> {
                    logger.debug("Saving message processed: {}", emailMessageDocument);
                    // Get message status type
                    var messageStatusTypeTO = messageStatusTypeService.findByName(emailMessageDocument.deliveryStatus().name());
                    var templateDefinedTO = templateDefinedService.find(emailMessageDocument.templateDefinedId());
                    // Create the message record
                    logger.debug("Creating message record: {}", emailMessageDocument);
                    var messageRecordTO = messageRecordService.create(messageRecordMapper.toMessageRecordTO(emailMessageDocument, messageStatusTypeTO));
                    logger.debug("Message record created: {}", messageRecordTO);
                    // If message type equal to verification code, create the verification code record
                    if (templateDefinedTO.template().templateType().id().equals(verificationCodeId)) {
                        // Create the verification code record
                        logger.debug("Creating verification code: {}", emailMessageDocument);
                        verificationCodeService.create(getVerificationCodeTO(emailMessageDocument, templateDefinedTO, messageRecordTO));
                        logger.debug("Verification code created: {}", emailMessageDocument);
                    }
                    // Delete the message from mongo db
                    emailService.delete(emailMessageDocument);
                    logger.debug("Message processed saved and updated: {}", emailMessageDocument);
                });
    }

    private VerificationCodeTO getVerificationCodeTO(EmailMessageDocument emailMessageDocument,
                                                     TemplateDefinedTO templateDefinedTO, MessageRecordTO messageRecordTO) {
        return new VerificationCodeTO(null,
                emailMessageDocument.userId(),
                templateDefinedTO.applicationId(),
                emailMessageDocument.params().get("vc").toString(),
                emailMessageDocument.sendDate(),
                emailMessageDocument.sendDate(),
                emailMessageDocument.sendDate().plusDays(2),
                null,
                false,
                0,
                3,
                "",
                "",
                messageRecordTO.id());
    }
}
