package com.prx.mercury.api.v1.service;

import com.prx.mercury.api.v1.to.EmailContact;
import com.prx.mercury.api.v1.to.TemplateDefinedTO;
import com.prx.mercury.api.v1.to.TemplateTO;
import com.prx.mercury.api.v1.to.TemplateTypeTO;
import com.prx.mercury.constant.DeliveryStatusType;
import com.prx.mercury.jpa.nosql.entity.EmailMessageDocument;
import com.prx.mercury.jpa.nosql.repository.EmailMessageNSRepository;
import freemarker.template.Configuration;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class EmailServiceImplTest {

    @Mock
    private JavaMailSender javaMailSender;

    @Mock
    private Configuration freemarkerConfig;

    @Mock
    private EmailMessageNSRepository emailMessageNSRepository;

    @InjectMocks
    private EmailServiceImpl emailServiceImpl;

    @Test
    @DisplayName("Find by delivery status with valid status")
    void findByDeliveryStatusWithValidStatus() {
        String id = UUID.randomUUID().toString();
        UUID messageId = UUID.randomUUID();
        UUID templateDefinedId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        String from = "test@example.com";
        List<EmailContact> to = List.of(new EmailContact("to@example.com", "To Name", "To Type"));
        List<EmailContact> cc = List.of(new EmailContact("cc@example.com", "Cc Name", "Cc Type"));
        String subject = "Test Subject";
        String body = "Test Body";
        LocalDateTime sendDate = LocalDateTime.now();
        Map<String, Object> params = Map.of("key", "value");
        DeliveryStatusType deliveryStatus = DeliveryStatusType.SENT;

        EmailMessageDocument emailMessageDocument = new EmailMessageDocument(
                id, messageId, templateDefinedId, userId, from, to, cc, subject, body, sendDate, params, deliveryStatus
        );
        List<EmailMessageDocument> expectedDocuments = List.of(emailMessageDocument);

        when(emailMessageNSRepository.findByDeliveryStatus(any(DeliveryStatusType.class))).thenReturn(expectedDocuments);

        List<EmailMessageDocument> result = emailServiceImpl.findByDeliveryStatus(deliveryStatus);

        assertNotNull(result);
        assertEquals(expectedDocuments, result);
        verify(emailMessageNSRepository).findByDeliveryStatus(deliveryStatus);
    }

    @Test
    @DisplayName("Find by delivery status with null status")
    void findByDeliveryStatusWithNullStatus() {
        assertThrows(IllegalArgumentException.class, () -> emailServiceImpl.findByDeliveryStatus(null));
    }

    @Test
    @DisplayName("Update email status with valid document")
    void updateEmailStatusWithValidDocument() {
        String id = UUID.randomUUID().toString();
        UUID messageId = UUID.randomUUID();
        UUID templateDefinedId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        String from = "test@example.com";
        List<EmailContact> to = List.of(new EmailContact("to@example.com", "To Name", "To Type"));
        List<EmailContact> cc = List.of(new EmailContact("cc@example.com", "Cc Name", "Cc Type"));
        String subject = "Test Subject";
        String body = "Test Body";
        LocalDateTime sendDate = LocalDateTime.now();
        Map<String, Object> params = Map.of("key", "value");
        DeliveryStatusType deliveryStatus = DeliveryStatusType.SENT;

        EmailMessageDocument emailMessageDocument = new EmailMessageDocument(
                id, messageId, templateDefinedId, userId, from, to, cc, subject, body, sendDate, params, deliveryStatus
        );

        emailServiceImpl.updateEmailStatus(emailMessageDocument);

        verify(emailMessageNSRepository).save(emailMessageDocument);
    }

    @Test
    @DisplayName("Update email status with null document")
    void updateEmailStatusWithNullDocument() {
        assertThrows(IllegalArgumentException.class, () -> emailServiceImpl.updateEmailStatus(null));
    }

    @Test
    @DisplayName("Delete email with valid document")
    void deleteEmailWithValidDocument() {
        String id = UUID.randomUUID().toString();
        UUID messageId = UUID.randomUUID();
        UUID templateDefinedId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        String from = "test@example.com";
        List<EmailContact> to = List.of(new EmailContact("to@example.com", "To Name", "To Type"));
        List<EmailContact> cc = List.of(new EmailContact("cc@example.com", "Cc Name", "Cc Type"));
        String subject = "Test Subject";
        String body = "Test Body";
        LocalDateTime sendDate = LocalDateTime.now();
        Map<String, Object> params = Map.of("key", "value");
        DeliveryStatusType deliveryStatus = DeliveryStatusType.SENT;

        EmailMessageDocument emailMessageDocument = new EmailMessageDocument(
                id, messageId, templateDefinedId, userId, from, to, cc, subject, body, sendDate, params, deliveryStatus
        );

        emailServiceImpl.delete(emailMessageDocument);

        verify(emailMessageNSRepository).delete(emailMessageDocument);
    }

    @Test
    @DisplayName("Send email with valid data")
    void sendEmailWithValidData() throws IOException {
        String id = UUID.randomUUID().toString();
        UUID messageId = UUID.randomUUID();
        UUID templateDefinedId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        String from = "test@example.com";
        List<EmailContact> to = List.of(new EmailContact("to@example.com", "To Name", "To Type"));
        List<EmailContact> cc = List.of(new EmailContact("cc@example.com", "Cc Name", "Cc Type"));
        String subject = "Test Subject";
        String body = "Test Body";
        LocalDateTime sendDate = LocalDateTime.now();
        Map<String, Object> params = Map.of("key", "value");
        DeliveryStatusType deliveryStatus = DeliveryStatusType.SENT;

        EmailMessageDocument emailMessageDocument = new EmailMessageDocument(
                id, messageId, templateDefinedId, userId, from, to, cc, subject, body, sendDate, params, deliveryStatus
        );
        UUID templateId = UUID.randomUUID();
        String description = "Template Description";
        String location = "Template Location";
        String fileFormat = "Template File Format";
        TemplateTypeTO templateType = new TemplateTypeTO(UUID.randomUUID(), "Template Type", "Description", LocalDateTime.now(), LocalDateTime.now(), true);
        UUID application = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();
        Boolean isActive = true;

        TemplateTO templateTO = new TemplateTO(
                templateId, description, location, fileFormat, templateType, application, now, now, isActive
        );
        TemplateDefinedTO templateDefinedTO = new TemplateDefinedTO(
                UUID.randomUUID(), templateTO, userId, application, now, now, now.plusDays(1), isActive, UUID.randomUUID()
        );

        when(freemarkerConfig.getTemplate(anyString())).thenReturn(mock(freemarker.template.Template.class));
        when(javaMailSender.createMimeMessage()).thenReturn(mock(jakarta.mail.internet.MimeMessage.class));

        EmailMessageDocument result = emailServiceImpl.sendEmail(emailMessageDocument, templateDefinedTO);

        assertNotNull(result);
        assertEquals(DeliveryStatusType.SENT, result.deliveryStatus());
    }

}
