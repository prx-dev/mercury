package com.prx.mercury.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

/**
 * FreeMakerConfig.
 *
 * @author Luis Antonio Mata
 * @version 1.0.0, 03-05-2022
 * @since 11
 */
@Configuration
@RequiredArgsConstructor
public class MailConfig {
    @Value("${spring.mail.port}")
    private Integer port;
    @Value("${spring.mail.host}")
    private String host;
    @Value("${spring.mail.username}")
    private String username;
    @Value("${spring.mail.password}")
    private String password;
    @Value("${spring.mail.protocol}")
    private String protocol;
    @Value("${spring.mail.properties.mail.timeout}")
    private int timeout;
    @Value("${spring.mail.properties.mail.starttls.enable}")
    private boolean starttls;
    @Value("${spring.mail.properties.mail.smtp.auth}")
    private boolean auth;

    /**
     * Create an instance from object type {@link JavaMailSender}
     * @return Object type {@link JavaMailSender}.
     */
    @Bean
    public JavaMailSender getJavaMailSender(){
        final var javaMailSenderImpl = new JavaMailSenderImpl();
        Properties props = new Properties();
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", port);
        props.put("mail.smtp.auth", auth);
        props.put("mail.smtp.starttls.enable", starttls);
        props.put("mail.smtp.user", username);
        props.put("mail.smtp.timeout", timeout);
        javaMailSenderImpl.setPassword(password);
        javaMailSenderImpl.setProtocol(protocol);
        javaMailSenderImpl.setJavaMailProperties(props);
        return javaMailSenderImpl;
    }
}
