/*
 * @(#)$file.className.java.
 *
 * Copyright (c) Luis Antonio Mata Mata. All rights reserved.
 *
 * All rights to this product are owned by Luis Antonio Mata Mata and may only
 * be used under the terms of its associated license document. You may NOT
 * copy, modify, sublicense, or distribute this source file or portions of
 * it unless previously authorized in writing by Luis Antonio Mata Mata.
 * In any event, this notice and the above copyright must always be included
 * verbatim with this file.
 */
package com.prx.mercury.api.v1.controller;

import com.prx.mercury.api.v1.service.EmailServiceImpl;
import com.prx.mercury.api.v1.to.SendEmailRequest;
import com.prx.mercury.api.v1.to.SendEmailResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

/// MailController.
/// This class handles HTTP requests related to sending emails.
/// It provides an endpoint to send an email using the EmailServiceImpl.
/// The controller is annotated with @RestController and @RequestMapping to define the base URL.
/// It also uses @CrossOrigin to allow cross-origin requests.
/// The email service implementation is injected via the constructor.
/// The username for the email service is injected from the application properties.
/// The send method handles POST requests to send an email.
/// It uses the @Operation and @ApiResponses annotations to document the API.
/// The method consumes and produces JSON and returns a ResponseEntity with the result of the email sending operation.
/// The request body is validated to ensure it is not null.
///
/// @version 1.0.0, 03-05-2022
/// @since 11
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("api/v1/mail")
public class MailController implements MailApi {

    private final EmailServiceImpl emailServiceImpl;

    /// Constructor for MailController.
    ///
    /// @param emailServiceImpl the email service implementation to be used for sending emails
    public MailController(EmailServiceImpl emailServiceImpl) {
        this.emailServiceImpl = emailServiceImpl;
    }

    /// Send an email.
    /// This method handles POST requests to send an email.
    /// It consumes and produces JSON.
    /// The request body is expected to be a RequestEntity containing a MailTO object.
    /// The method returns a ResponseEntity with the result of the email sending operation.
    ///
    /// @param requestMail the request entity containing the email details
    /// @return a ResponseEntity with the result of the email sending operation
    @Override
    public ResponseEntity<SendEmailResponse> send(@RequestBody SendEmailRequest requestMail) {
        return emailServiceImpl.sendMail(Objects.requireNonNull(requestMail));
    }

}
