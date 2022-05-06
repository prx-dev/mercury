/*
 *
 *  * @(#)CustomKeycloakSpringBootConfigResolver.java.
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

import org.keycloak.adapters.KeycloakDeployment;
import org.keycloak.adapters.KeycloakDeploymentBuilder;
import org.keycloak.adapters.spi.HttpFacade;
import org.keycloak.adapters.springboot.KeycloakSpringBootConfigResolver;
import org.keycloak.adapters.springboot.KeycloakSpringBootProperties;
import org.springframework.context.annotation.Configuration;

/**
 * CustomKeycloakSpringBootConfigResolver.
 *
 * @author <a href="mailto:luis.antonio.mata@gmail.com">Luis Antonio Mata</a>
 * @version 1.0.1.20200904-01, 2020-08-19
 */
@Configuration
public class CustomKeycloakSpringBootConfigResolver extends KeycloakSpringBootConfigResolver {
    private final KeycloakDeployment keycloakDeployment;

    /**
     * Constructor
     *
     * @param properties {@link KeycloakSpringBootProperties}
     */
    public CustomKeycloakSpringBootConfigResolver(KeycloakSpringBootProperties properties) {
        super();
        keycloakDeployment = KeycloakDeploymentBuilder.build(properties);
    }

    /**
     * Recibe las solicitudes para aplicar filtros de seguridad y validaci&oacute;n de acceso.
     *
     * @param facade {@link HttpFacade.Request}
     * @return {@link KeycloakDeployment}
     */
    @Override
    public KeycloakDeployment resolve(HttpFacade.Request facade) {
        return keycloakDeployment;
    }
}
