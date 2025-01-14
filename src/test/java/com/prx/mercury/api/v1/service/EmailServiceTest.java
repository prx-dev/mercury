package com.prx.mercury.api.v1.service;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;


class EmailServiceTest {

    private final EmailService emailService = new EmailService() {
    };

    @Test
    void sendMailShouldReturnNotImplemented() {
        var response = emailService.sendMail(null);
        assertEquals(HttpStatus.NOT_IMPLEMENTED, response.getStatusCode());
    }

}
