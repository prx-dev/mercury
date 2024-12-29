package com.prx.mercury.api.v1.to;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;

/// A record that represents a request to send an email.
///
/// @param templateId The ID of the email template.
/// @param from The sender's email address.
/// @param to The list of recipient email addresses.
/// @param cc The list of CC email addresses.
/// @param subject The subject of the email.
/// @param body The body content of the email.
/// @param sendDate The date the email was sent.
/// @param params Additional parameters for the email.
public record SendEmailRequest (
        @NotNull @NotBlank @NotEmpty String templateId,
        @NotNull @NotBlank @NotEmpty String from,
        List<String> to,
        List<String> cc,
        @NotNull @NotBlank @NotEmpty String subject,
        @NotNull @NotBlank @NotEmpty String body,
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime sendDate,
        Map<String, Object> params
){

    @Override
    public String toString() {
        return "SendEmailRequest{" +
                "templateId='" + templateId + '\'' +
                ", from='" + from + '\'' +
                ", to=" + to +
                ", cc=" + cc +
                ", subject='" + subject + '\'' +
                ", body='" + body + '\'' +
                ", sendDate=" + sendDate +
                ", params=" + params +
                '}';
    }
}
