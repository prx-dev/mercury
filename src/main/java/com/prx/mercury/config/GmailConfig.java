package com.prx.mercury.config;

        import com.google.api.client.auth.oauth2.BearerToken;
        import com.google.api.client.auth.oauth2.Credential;
        import com.google.api.client.auth.oauth2.TokenResponse;
        import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
        import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
        import com.google.api.client.http.HttpTransport;
        import com.google.api.client.json.JsonFactory;
        import com.google.api.client.json.gson.GsonFactory;
        import com.google.api.services.gmail.Gmail;
        import com.prx.commons.constants.types.MessageType;
        import com.prx.commons.exception.StandardException;
        import com.prx.mercury.api.v1.to.GmailCredential;
        import org.slf4j.Logger;
        import org.slf4j.LoggerFactory;
        import org.springframework.beans.factory.annotation.Value;
        import org.springframework.context.annotation.Bean;
        import org.springframework.context.annotation.Configuration;
        import org.springframework.http.HttpEntity;
        import org.springframework.http.HttpStatus;
        import org.springframework.http.ResponseEntity;
        import org.springframework.web.client.RestTemplate;
        import org.springframework.web.server.ResponseStatusException;

        import java.io.IOException;
        import java.security.GeneralSecurityException;
        import java.util.Map;
        import java.util.Objects;

        import static org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames.*;

        /**
         * Configuration class for Gmail API connection.
         * Manages authentication credentials and provides a configured Gmail API client.
         *
         * @version 1.0.0
         * @since 11
         */
        @Configuration
        public class GmailConfig {

            private static final Logger logger = LoggerFactory.getLogger(GmailConfig.class);

            private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
            private GmailCredential gmailCredential;

            /**
             * Google OAuth2 client ID injected from application properties.
             */
            @Value("${spring.google.client-id}")
            private String clientId;

            /**
             * Google OAuth2 client secret injected from application properties.
             */
            @Value("${spring.google.client-secret}")
            private String secretKey;

            /**
             * Google OAuth2 refresh token injected from application properties.
             */
            @Value("${spring.google.refresh-token}")
            private String refreshToken;

            /**
             * Email address to be used as the sender's address.
             */
            @Value("${spring.google.from-email}")
            private String fromEmail;

            /**
             * Google OAuth2 token endpoint URI.
             */
            @Value("${spring.google.token-uri}")
            private String tokenUri;

            private final RestTemplate restTemplate;

            /**
             * HTTP transport instance for API communication.
             */
            HttpTransport httpTransport;

            /**
             * Constructs a GmailConfig with the required RestTemplate dependency.
             * Initializes the HTTP transport during construction.
             *
             * @param restTemplate REST client for making HTTP requests to the token endpoint
             */
            public GmailConfig(RestTemplate restTemplate) {
                this.restTemplate = restTemplate;
                this.init();
            }

            /**
             * Provides the Gmail credential configuration.
             * Creates the credential object if it doesn't exist.
             *
             * @return A configured GmailCredential object
             */
            @Bean
            public GmailCredential getGmailCredential() {
                if (gmailCredential == null) {
                    gmailCredential = new GmailCredential(
                            clientId,
                            secretKey,
                            refreshToken,
                            null,
                            null,
                            fromEmail
                    );
                }
                return gmailCredential;
            }

            /**
             * Initializes the Google HTTP transport with trusted certificates.
             * Called during construction to set up secure communication.
             *
             * @throws StandardException if initialization fails due to security or I/O errors
             */
            private void init() {
                try {
                    logger.debug("Initializing Gmail configuration...");
                    httpTransport = GoogleNetHttpTransport.newTrustedTransport();
                } catch (GeneralSecurityException | IOException e) {
                    logger.warn("Error while initializing Gmail configuration. {}", e.getMessage());
                    throw new StandardException(MessageType.DEFAULT_MESSAGE, e);
                }
            }

            /**
             * Creates OAuth2 credentials for Gmail API authentication.
             * Uses the refresh token to obtain a new access token.
             *
             * @return OAuth2 credential with bearer token authentication
             * @throws ResponseStatusException if unable to obtain credentials
             */
            private Credential getCredentials() {
                try {
                    return new Credential(BearerToken.authorizationHeaderAccessMethod()).setFromTokenResponse(refreshToken());
                } catch (Exception e) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error while getting credentials, not able to process request.");
                }
            }

            /**
             * Creates and configures a Gmail API client instance.
             * Uses the credentials provided by {@link #getCredentials()}.
             *
             * @return A configured Gmail API client
             */
            @Bean
            public Gmail getGmail() {
                return new Gmail.Builder(httpTransport, JSON_FACTORY, getCredentials())
                        .build();
            }

            /**
             * Refreshes the OAuth2 token by making a request to the token endpoint.
             * Updates the gmailCredential with the new access token.
             *
             * @return A token response containing the refreshed access token
             * @throws ResponseStatusException if token refresh fails or returns invalid data
             */
            private TokenResponse refreshToken() {

                GmailCredential gmailCredentialLocal = new GmailCredential(
                        clientId,
                        secretKey,
                        refreshToken,
                        REFRESH_TOKEN,
                        null,
                        null
                );

                HttpEntity<GmailCredential> request = new HttpEntity<>(gmailCredentialLocal);

                try {
                    ResponseEntity<Map> mapTokenResponse = restTemplate.postForEntity(
                            tokenUri,
                            request,
                            Map.class);

                    if (mapTokenResponse.getStatusCode() != HttpStatus.OK
                            || Objects.isNull(mapTokenResponse.getBody())
                            || !mapTokenResponse.getBody().containsKey(ACCESS_TOKEN)
                            || !mapTokenResponse.getBody().containsKey(EXPIRES_IN)
                            || !mapTokenResponse.getBody().containsKey(REFRESH_TOKEN)
                            || !mapTokenResponse.getBody().containsKey(SCOPE)
                    ) {
                        logger.error("Error while refreshing token, not able to process request.");
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error while refreshing token, not able to process request.");
                    }
                    GoogleTokenResponse tokenResponse = new GoogleTokenResponse();
                    tokenResponse.setAccessToken((String) mapTokenResponse.getBody().get(ACCESS_TOKEN));
                    tokenResponse.setExpiresInSeconds(Long.parseLong(mapTokenResponse.getBody().get(EXPIRES_IN).toString()));
                    tokenResponse.setRefreshToken((String) mapTokenResponse.getBody().get(REFRESH_TOKEN));
                    tokenResponse.setScope((String) mapTokenResponse.getBody().get(SCOPE));

                    gmailCredential = new GmailCredential(
                            clientId,
                            secretKey,
                            refreshToken,
                            REFRESH_TOKEN,
                            tokenResponse.getAccessToken(),
                            fromEmail
                    );
                    return tokenResponse;
                } catch (Exception e) {
                    logger.error("Error while refreshing token, not able to process request. {}", e.getMessage());
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error while refreshing token, not able to process request.");
                }
            }
        }
