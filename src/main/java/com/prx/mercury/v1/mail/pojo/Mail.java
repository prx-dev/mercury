package com.prx.mercury.v1.mail.pojo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Mail.
 *
 * @author Luis Antonio Mata
 * @version 1.0.0, 03-05-2022
 * @since 11
 */
@Data
public class Mail implements Serializable {
    private String templateId;
    private String from;
    private List<String> to;
    private List<String> cc;
    private String subject;
    private String body;
    private Date sentDate;
    private transient Map<String, Object> params;
}
