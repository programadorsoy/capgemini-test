package com.capgemini.test.code.config;

import org.springframework.context.annotation.Configuration;

/**
 * Feign se autoconfigura vía @EnableFeignClients en CodeApplication.
 * Esta clase se deja por si en el futuro se añaden RestClient/WebClient.
 */
@Configuration
public class RestClientConfig {
}