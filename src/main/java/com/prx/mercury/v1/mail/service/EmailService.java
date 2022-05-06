package com.prx.mercury.v1.mail.service;

import com.prx.mercury.v1.mail.pojo.Mail;
import org.springframework.http.ResponseEntity;

/**
 * EmailService.
 *
 * @author Luis Antonio Mata
 * @version 1.0.0, 05-05-2022
 * @since 11
 */
public interface EmailService {
    ResponseEntity<String> sendMail(Mail mail);
}
