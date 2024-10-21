package io.github.deniskonev.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig {

    @Value("${external.api.username}")
    private String externalApiUsername;

    @Value("${external.api.password}")
    private String externalApiPassword;

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder
                .basicAuthentication(externalApiUsername, externalApiPassword) // Использование учётных данных из properties
                .build();
    }
}
