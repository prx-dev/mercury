package com.prx.mercury.api.v1.service;

import com.prx.mercury.api.v1.to.EmailContact;
import com.prx.mercury.api.v1.to.SendEmailRequest;
import com.prx.mercury.api.v1.to.SendEmailResponse;
import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import jakarta.mail.MessagingException;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import java.io.IOException;
import java.util.Objects;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

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

    private final JavaMailSender javaMailSender;
    private final Configuration freemarkerConfig;

    /// Constructor for EmailServiceImpl.
    ///
    /// @param javaMailSender   the JavaMailSender to be used for sending emails
    /// @param freemarkerConfig the FreeMarker configuration for processing email templates
    public EmailServiceImpl(JavaMailSender javaMailSender, Configuration freemarkerConfig) {
        this.javaMailSender = javaMailSender;
        this.freemarkerConfig = freemarkerConfig;
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
        final var mimeMessage = javaMailSender.createMimeMessage();

        try {
            //TODO - Pending change to load template by template ID
            var body = FreeMarkerTemplateUtils.processTemplateIntoString(
                    freemarkerConfig.getTemplate(mail.templateId()), mail.params());
            final var mimeMessageHelper = new MimeMessageHelper(mimeMessage, MULTIPART_MODE_MIXED_RELATED, UTF_8.name());
            mimeMessageHelper.setText(body, true);
            mimeMessageHelper.setSubject(mail.subject());
            mimeMessageHelper.setFrom(mail.from());
            mimeMessageHelper.setTo((String[]) mail.to().stream().filter(Objects::nonNull).map(EmailContact::email).toArray());
            if(!mail.cc().isEmpty()) {
                mimeMessageHelper.setCc((String[]) mail.cc().stream().filter(Objects::nonNull).map(EmailContact::email).toArray());
            }
            // Pending include logic to store and retrieve email ID
            javaMailSender.send(mimeMessage);
            return ResponseEntity.ok(createResponse(mail));
        } catch (IOException | TemplateException | MessagingException ex) {
            Logger.getLogger(EmailServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ResponseEntity.badRequest().build();
    }

    private SendEmailResponse createResponse(SendEmailRequest requestMail) {
        return new SendEmailResponse(UUID.randomUUID(), "Delivered", requestMail.body());
    }

}
