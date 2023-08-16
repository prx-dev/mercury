/*
 *
 *  * @(#)SecurityConfig.java.
 *  *
 *  * Copyright (c) Luis Antonio Mata Mata. All rights reserved.
 *  *
 *  * All rights to this product are owned by Luis Antonio Mata Mata and may only
 *  * be used under the terms of its associated license document. You may NOT
 *  * copy, modify, sublicense, or distribute this source file or portions of
 *  * it unless previously authorized in writing by Luis Antonio Mata Mata.
 *  * In any event, this notice and the above copyright must always be included
 *  * verbatim with this file.
 *
 */

package com.prx.mercury.config.keycloak;

import lombok.RequiredArgsConstructor;
import org.keycloak.adapters.KeycloakConfigResolver;
import org.keycloak.adapters.springboot.KeycloakSpringBootProperties;
import org.keycloak.adapters.springsecurity.KeycloakSecurityComponents;
import org.keycloak.adapters.springsecurity.authentication.KeycloakAuthenticationProvider;
import org.keycloak.adapters.springsecurity.client.KeycloakClientRequestFactory;
import org.keycloak.adapters.springsecurity.client.KeycloakRestTemplate;
import org.keycloak.adapters.springsecurity.config.KeycloakWebSecurityConfigurerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.*;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.authority.mapping.SimpleAuthorityMapper;
import org.springframework.security.web.authentication.session.NullAuthenticatedSessionStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="mailto:luis.antonio.mata@gmail.com">Luis Antonio Mata</a>
 * @version 1.0.1.20200904-01, 26-10-2020
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@ComponentScan(basePackageClasses = KeycloakSecurityComponents.class)
public class SecurityConfig extends KeycloakWebSecurityConfigurerAdapter {
    private static final Logger LOGGER = LoggerFactory.getLogger(SecurityConfig.class);

    private final KeycloakSpringBootProperties properties;

    private final KeycloakClientRequestFactory keycloakClientRequestFactory;

    @Bean
    @Override
    protected SessionAuthenticationStrategy sessionAuthenticationStrategy() {
        return new NullAuthenticatedSessionStrategy();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        super.configure(http);
        List<String> roles = new ArrayList<>();
        List<String> methods = new ArrayList<>();
        List<String> patterns = new ArrayList<>();
        try {
            properties.getSecurityConstraints().forEach(securityConstraint -> {
                roles.addAll(securityConstraint.getAuthRoles());
                securityConstraint.getSecurityCollections().forEach(
                        securityCollection -> securityCollection.getMethods().forEach(s -> methods.add(s.intern())));
                securityConstraint.getSecurityCollections().forEach(
                        securityCollection -> securityCollection.getPatterns().forEach(s -> patterns.add(s.intern())));
                LOGGER.debug("Valida accesos en configure, roles {}, metodos {}, patrones {}", roles, methods, patterns);
            });
            if (!patterns.isEmpty() && !roles.isEmpty()) {
                http.anonymous().and().cors().and().authorizeRequests().
                        antMatchers("/general/*").permitAll().and()
                        .authorizeRequests()
                        .antMatchers(HttpMethod.GET, patterns.toArray(new String[0]))
                        .hasAnyRole(roles.toArray(new String[0]))
                        .antMatchers(HttpMethod.POST, patterns.toArray(new String[0]))
                        .hasAnyRole(roles.toArray(new String[0]))
                        .anyRequest().permitAll().and().csrf().disable();
                LOGGER.debug("PERMISOS CARGADOS, roles {}, metodos {}, patrones {}", roles, methods, patterns);
            }
        }catch (ClassCastException e) {
            LOGGER.warn("Ha ocurrido un error por error en lectura de clase", e);
            throw e;
        }catch (Exception e) {
            LOGGER.warn("Ha ocurrido un error durante la carga de permisos y roles",e);
            throw e;
        }

        LOGGER.debug("Termina llamado a configure");
    }

    @Bean
    public WebMvcConfigurer corsConfiguration() {
        return new WebMvcConfigurerAdapter() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/general/*")
                        .allowedMethods(HttpMethod.GET.toString(), HttpMethod.POST.toString(),
                                HttpMethod.PUT.toString(), HttpMethod.DELETE.toString(), HttpMethod.OPTIONS.toString())
                        .allowedOrigins("*");
            }
        };
    }

    public void configureGlobal(AuthenticationManagerBuilder auth) {
        KeycloakAuthenticationProvider keycloakAuthenticationProvider = keycloakAuthenticationProvider();
        keycloakAuthenticationProvider.setGrantedAuthoritiesMapper(new SimpleAuthorityMapper());
        auth.authenticationProvider(keycloakAuthenticationProvider);
    }

    @Primary
    @Bean
    public KeycloakConfigResolver keycloakConfigResolver(KeycloakSpringBootProperties properties){
        return new CustomKeycloakSpringBootConfigResolver(properties);
    }


    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public KeycloakRestTemplate keycloakRestTemplate(){
        return new KeycloakRestTemplate(keycloakClientRequestFactory);
    }

}
