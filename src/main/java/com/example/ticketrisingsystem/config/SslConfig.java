package com.example.ticketrisingsystem.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * SSL/TLS Configuration for HTTPS support
 * Enable this profile using: spring.profiles.active=ssl OR spring.profiles.active=prod
 * 
 * SSL configuration is handled through application.properties files:
 * - For development: use application-ssl.properties
 * - For production: use application-prod.properties
 */
@Slf4j
@Configuration
public class SslConfig {

    @Value("${server.ssl.enabled:false}")
    private boolean sslEnabled;

    @Value("${server.ssl.key-store:classpath:keystore.p12}")
    private String keyStore;

    @Value("${server.ssl.key-store-type:PKCS12}")
    private String keyStoreType;

    @Value("${server.port:8080}")
    private int serverPort;
}
