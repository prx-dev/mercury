package com.prx.mercury.api.v1.service;

import com.prx.mercury.api.v1.to.*;
import com.prx.mercury.constant.DeliveryStatusType;
import com.prx.mercury.jpa.nosql.entity.EmailMessageDocument;
import com.prx.mercury.jpa.sql.entity.MessageStatusTypeEntity;
import com.prx.mercury.jpa.sql.repository.FrequencyTypeRepository;
import com.prx.mercury.jpa.sql.repository.MessageRecordRepository;
import com.prx.mercury.jpa.sql.repository.MessageStatusTypeEntityRepository;
import com.prx.mercury.jpa.sql.repository.TemplateDefinedRepository;
import com.prx.mercury.mapper.MessageRecordMapper;
import com.prx.mercury.mapper.TemplateDefinedMapper;
import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import jakarta.mail.MessagingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.function.IntFunction;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.springframework.mail.javamail.MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED;

/// EmailServiceImpl.
/// This class provides the implementation for sending emails using JavaMailSender and FreeMarker templates.
/// It implements the EmailService interface.
/// The class is annotated with @Service to indicate that it is a Spring service component.
/// The sender email address is injected from the application properties.
/// The sendMail method handles the creation and sending of the email.
/// It processes the email template using FreeMarker and sets the email details using MimeMessageHelper.
/// The method returns a ResponseEntity with the result of the email sending operation.
/// If an exception occurs, it logs the error and returns a bad request response.
///
/// @version 1.0.0, 03-05-2022
/// @since 11
@Service
public class EmailServiceImpl implements EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailServiceImpl.class);
    private final JavaMailSender javaMailSender;
    private final Configuration freemarkerConfig;

    private final TemplateDefinedRepository templateDefinedRepository;
    private final TemplateDefinedMapper templateDefinedMapper;
    private final MessageRecordRepository messageRecordRepository;
    private final MessageRecordMapper messageRecordMapper;
    private final FrequencyTypeRepository frequencyTypeRepository;
    private final MessageStatusTypeEntityRepository messageStatusTypeEntityRepository;

    /// Constructor for EmailServiceImpl.
    ///
    /// @param javaMailSender   the JavaMailSender to be used for sending emails
    /// @param freemarkerConfig the FreeMarker configuration for processing email templates
    public EmailServiceImpl(JavaMailSender javaMailSender, Configuration freemarkerConfig,
                            TemplateDefinedRepository templateDefinedRepository,
                            TemplateDefinedMapper templateDefinedMapper, MessageRecordRepository messageRecordRepository, MessageRecordMapper messageRecordMapper, FrequencyTypeRepository frequencyTypeRepository,
                            MessageStatusTypeEntityRepository messageStatusTypeEntityRepository) {
        this.templateDefinedRepository = templateDefinedRepository;
        this.messageRecordRepository = messageRecordRepository;
        this.templateDefinedMapper = templateDefinedMapper;
        this.freemarkerConfig = freemarkerConfig;
        this.javaMailSender = javaMailSender;
        this.messageRecordMapper = messageRecordMapper;
        this.frequencyTypeRepository = frequencyTypeRepository;
        this.messageStatusTypeEntityRepository = messageStatusTypeEntityRepository;
    }

    /// Send an email.
    /// This method handles the creation and sending of an email.
    /// It processes the email template using FreeMarker and sets the email details using MimeMessageHelper.
    /// The method returns a ResponseEntity with the result of the email sending operation.
    /// If an exception occurs, it logs the error and returns a bad request response.
    ///
    /// @param mail the MailTO object containing the email details
    /// @return a ResponseEntity with the result of the email sending operation
    @Override
    public ResponseEntity<SendEmailResponse> sendMail(SendEmailRequest mail) {
        final var templateDefinedTO = loadTemplateDefined(mail.templateDefinedId());
        if (Objects.isNull(templateDefinedTO)) {
            return ResponseEntity.badRequest().build();
        }
        return process(templateDefinedTO.template(), mail.subject(), mail.from(), mail.to(), mail.cc(), mail.params()) ?
                ResponseEntity.ok(createResponse(mail)) :
                ResponseEntity.badRequest().build();
    }

    @Override
    public EmailMessageDocument sendEmail(EmailMessageDocument emailMessageDocument) {
        final var templateDefinedTO = loadTemplateDefined(emailMessageDocument.templateDefinedId());
        if (Objects.isNull(templateDefinedTO)) {
            var error = new IllegalArgumentException("Template defined not found");
            logger.error(error.getMessage(), error);
            throw error;
        }
        var result = process(templateDefinedTO.template(), emailMessageDocument.subject(), emailMessageDocument.from(),
                emailMessageDocument.to(), emailMessageDocument.cc(), emailMessageDocument.params());
        return new EmailMessageDocument(
                emailMessageDocument.id(),
                emailMessageDocument.templateDefinedId(),
                emailMessageDocument.userId(),
                emailMessageDocument.from(),
                emailMessageDocument.to(),
                emailMessageDocument.cc(),
                emailMessageDocument.subject(),
                emailMessageDocument.body(),
                emailMessageDocument.sendDate(),
                emailMessageDocument.params(),
                result ? DeliveryStatusType.SENT : DeliveryStatusType.FAILED);
    }

    //    @Cacheable(cacheManager = "templateDefinedCacheManager")
    @Override
    public TemplateDefinedTO loadTemplateDefined(UUID templateDefinedId) {
        var optionalTemplateEntity = templateDefinedRepository.findById(templateDefinedId);
        if (optionalTemplateEntity.isEmpty()) {
            throw new IllegalArgumentException("Template defined not found");
        }
        return templateDefinedMapper.toTemplateDefinedTO(optionalTemplateEntity.get());
    }

    @Override
    public void saveMessageRecord(EmailMessageDocument emailMessageDocument) {
        MessageStatusTypeEntity messageStatusTypeEntity = messageStatusTypeEntityRepository.findByName(emailMessageDocument.deliveryStatus().toString());
        var optionalTemplateEntity = templateDefinedRepository.findById(emailMessageDocument.templateDefinedId());
        if (optionalTemplateEntity.isEmpty()) {
            throw new IllegalArgumentException("Template defined not found");
        }

        if (Objects.isNull(messageStatusTypeEntity)) {
            throw new IllegalArgumentException("Message status type not found");
        }
        var messageRecordEntity = messageRecordMapper.toMessageRecordEntity(emailMessageDocument,  messageStatusTypeEntity, optionalTemplateEntity.get());

        messageRecordRepository.save(messageRecordEntity);

    }

    private SendEmailResponse createResponse(SendEmailRequest requestMail) {
        return new SendEmailResponse(UUID.randomUUID(), DeliveryStatusType.SENT.toString(), requestMail.body());
    }

    private boolean process(TemplateTO templateTO, String subject, String from, List<EmailContact> to, List<EmailContact> cc, Map<String, Object> params) {
        final IntFunction<String[]> function = String[]::new;
        final var mimeMessage = javaMailSender.createMimeMessage();
        boolean isProcessed = false;

        try {
            var body = FreeMarkerTemplateUtils.processTemplateIntoString(
                    freemarkerConfig.getTemplate(templateTO.location()), params);
            final var mimeMessageHelper = new MimeMessageHelper(mimeMessage, MULTIPART_MODE_MIXED_RELATED, UTF_8.name());
            mimeMessageHelper.setText(body, true);
            mimeMessageHelper.setSubject(subject);
            mimeMessageHelper.setFrom(from);
            mimeMessageHelper.setTo(to.stream().filter(Objects::nonNull).map(EmailContact::email).toArray(function));
            if (!cc.isEmpty()) {
                var ccList = cc.stream().filter(Objects::nonNull).map(EmailContact::email).toArray(function);
                mimeMessageHelper.setCc(ccList);
            }
            // Pending include logic to store and retrieve email ID
            javaMailSender.send(mimeMessage);
            isProcessed = true;
        } catch (IOException | TemplateException | MessagingException ex) {
            logger.error("Error sending email: {}", ex.getMessage());
        }
        return isProcessed;
    }

}
