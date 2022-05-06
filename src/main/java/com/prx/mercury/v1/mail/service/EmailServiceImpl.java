package com.prx.mercury.v1.mail.service;

import com.prx.mercury.v1.mail.pojo.Mail;
import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.springframework.mail.javamail.MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED;

/**
 * EmailService.
 *
 * @author Luis Antonio Mata
 * @version 1.0.0, 03-05-2022
 * @since 11
 */
@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {
    private final JavaMailSender javaMailSender;
    @Value("${spring.mail.username}")
    private String sender;
    private final Configuration freemarkerConfig;

    /**
     *
     * @param mail Object type {@link Mail}
     */
    @Override
    public ResponseEntity<String> sendMail(Mail mail) {
        final var mimeMessage = javaMailSender.createMimeMessage();
        try {
            //TODO - Pending change to load template by template ID
            mail.setBody(FreeMarkerTemplateUtils.processTemplateIntoString(
                    freemarkerConfig.getTemplate(mail.getTemplateId()), mail.getParams()));
            if(!mail.getBody().isEmpty()) {
                final var mimeMessageHelper = new MimeMessageHelper(mimeMessage, MULTIPART_MODE_MIXED_RELATED, UTF_8.name());
                mimeMessageHelper.setText(mail.getBody(), true);
                mimeMessageHelper.setSubject(mail.getSubject());
                mimeMessageHelper.setFrom(mail.getFrom());
                mimeMessageHelper.setTo(mail.getTo().toArray(new String[0]));
                javaMailSender.send(mimeMessage);
                return ResponseEntity.ok().build();
            }
        } catch (MessagingException | IOException | TemplateException ex) {
            Logger.getLogger(EmailServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ResponseEntity.badRequest().build();
    }

}
