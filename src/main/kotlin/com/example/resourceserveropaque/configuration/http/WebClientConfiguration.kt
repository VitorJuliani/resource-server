package com.example.resourceserveropaque.configuration.http

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.oauth2.server.resource.web.reactive.function.client.ServletBearerExchangeFilterFunction
import org.springframework.web.reactive.function.client.WebClient

@Configuration
class WebClientConfiguration {

    @Bean
    fun client(): WebClient {
        return WebClient.builder()
            .filter(ServletBearerExchangeFilterFunction())
            .build()
    }
}
