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
package com.prx.mercury.v1.mail.api.controller;

import com.prx.mercury.v1.mail.pojo.Mail;
import com.prx.mercury.v1.mail.service.EmailServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

/**
 * MailController.
 *
 * @author Luis Antonio Mata
 * @version 1.0.0, 03-05-2022
 * @since 11
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@RequestMapping("v1/mail")
public class MailController {
    private final EmailServiceImpl emailServiceImpl;
    @Value("${spring.mail.username}")
    private String username;

    @Operation(description = "Send a email")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "To send a email.")
    })
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE, path = "/")
    public ResponseEntity<String> send(@RequestBody RequestEntity<Mail> requestMail) {
        return emailServiceImpl.sendMail(Objects.requireNonNull(requestMail.getBody()));
    }

}
