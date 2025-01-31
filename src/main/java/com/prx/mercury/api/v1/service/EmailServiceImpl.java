package com.prx.mercury.api.v1.service;

import com.prx.mercury.api.v1.to.EmailContact;
import com.prx.mercury.api.v1.to.TemplateDefinedTO;
import com.prx.mercury.api.v1.to.TemplateTO;
import com.prx.mercury.constant.DeliveryStatusType;
import com.prx.mercury.jpa.nosql.entity.EmailMessageDocument;
import com.prx.mercury.jpa.nosql.repository.EmailMessageNSRepository;
import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import jakarta.mail.MessagingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
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
    private final EmailMessageNSRepository emailMessageNSRepository;

    /// Constructor for EmailServiceImpl.
    ///
    /// @param javaMailSender   the JavaMailSender to be used for sending emails
    /// @param freemarkerConfig the FreeMarker configuration for processing email templates
    public EmailServiceImpl(JavaMailSender javaMailSender, Configuration freemarkerConfig, EmailMessageNSRepository emailMessageNSRepository
    ) {
        this.freemarkerConfig = freemarkerConfig;
        this.javaMailSender = javaMailSender;
        this.emailMessageNSRepository = emailMessageNSRepository;
    }

    @Override
    public List<EmailMessageDocument> findByDeliveryStatus(DeliveryStatusType deliveryStatus) {
        if(Objects.isNull(deliveryStatus)) {
            throw new IllegalArgumentException("Delivery status is required");
        }
        if(DeliveryStatusType.OPENED.equals(deliveryStatus) || DeliveryStatusType.SENT.equals(deliveryStatus)) {
            return emailMessageNSRepository.findByDeliveryStatus(deliveryStatus);
        }
        // PENDING - get the rest of the delivery status from the database and return the list
        return Collections.emptyList();
    }

    @Override
    public void updateEmailStatus(EmailMessageDocument emailMessageDocument) {
        if(Objects.isNull(emailMessageDocument)) {
            throw new IllegalArgumentException("Email message document is required");
        }
        emailMessageNSRepository.save(emailMessageDocument);
    }

    @Override
    public void delete(EmailMessageDocument emailMessageDocument) {
        emailMessageNSRepository.delete(emailMessageDocument);
    }

    @Override
    public EmailMessageDocument sendEmail(EmailMessageDocument emailMessageDocument, TemplateDefinedTO templateDefinedTO) {
        if (Objects.isNull(templateDefinedTO)) {
            var error = new IllegalArgumentException("Template defined not found");
            logger.error(error.getMessage(), error);
            throw error;
        }
        var result = process(templateDefinedTO.template(), emailMessageDocument.subject(), emailMessageDocument.from(),
                emailMessageDocument.to(), emailMessageDocument.cc(), emailMessageDocument.params());
        return new EmailMessageDocument(
                emailMessageDocument.id(),
                emailMessageDocument.messageId(),
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
            javaMailSender.send(mimeMessage);
            isProcessed = true;
        } catch (IOException | TemplateException | MessagingException ex) {
            logger.error("Error sending email: {}", ex.getMessage());
        }
        return isProcessed;
    }

}
