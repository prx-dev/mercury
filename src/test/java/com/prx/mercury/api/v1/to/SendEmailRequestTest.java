package com.prx.mercury.api.v1.to;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class SendEmailRequestTest {

    @Test
    @DisplayName("Create SendEmailRequest with valid data")
    void createSendEmailRequestWithValidData() {
        List<EmailContact> to = List.of(new EmailContact("to@example.com", "To Name", "To Alias"));
        List<EmailContact> cc = List.of(new EmailContact("cc@example.com", "Cc Name", "Cc Alias"));
        LocalDateTime sendDate = LocalDateTime.now();
        Map<String, Object> params = Map.of("key", "value");

        SendEmailRequest request = new SendEmailRequest(
                "templateId",
                "from@example.com",
                to,
                cc,
                "Subject",
                "Body",
                sendDate,
                params
        );

        assertEquals("templateId", request.templateId());
        assertEquals("from@example.com", request.from());
        assertEquals(to, request.to());
        assertEquals(cc, request.cc());
        assertEquals("Subject", request.subject());
        assertEquals("Body", request.body());
        assertEquals(sendDate, request.sendDate());
        assertEquals(params, request.params());
    }

    @Test
    @DisplayName("Create SendEmailRequest with null params")
    void createSendEmailRequestWithNullParams() {
        List<EmailContact> to = List.of(new EmailContact("to@example.com", "To Name", "To Alias"));
        List<EmailContact> cc = List.of(new EmailContact("cc@example.com", "Cc Name", "Cc Alias"));
        LocalDateTime sendDate = LocalDateTime.now();

        SendEmailRequest request = new SendEmailRequest(
                "templateId",
                "from@example.com",
                to,
                cc,
                "Subject",
                "Body",
                sendDate,
                null
        );

        assertEquals("templateId", request.templateId());
        assertEquals("from@example.com", request.from());
        assertEquals(to, request.to());
        assertEquals(cc, request.cc());
        assertEquals("Subject", request.subject());
        assertEquals("Body", request.body());
        assertEquals(sendDate, request.sendDate());
        assertNull(request.params());
    }

    @Test
    @DisplayName("SendEmailRequest toString method")
    void sendEmailRequestToString() {
        List<EmailContact> to = List.of(new EmailContact("to@example.com", "To Name", "To Alias"));
        List<EmailContact> cc = List.of(new EmailContact("cc@example.com", "Cc Name", "Cc Alias"));
        LocalDateTime sendDate = LocalDateTime.now();
        Map<String, Object> params = Map.of("key", "value");

        SendEmailRequest request = new SendEmailRequest(
                "templateId",
                "from@example.com",
                to,
                cc,
                "Subject",
                "Body",
                sendDate,
                params
        );

        String expected = "SendEmailRequest{templateId='templateId', from='from@example.com', to=" + to + ", cc=" + cc + ", subject='Subject', body='Body', sendDate=" + sendDate + ", params=" + params + "}";
        assertEquals(expected, request.toString());
    }
}
