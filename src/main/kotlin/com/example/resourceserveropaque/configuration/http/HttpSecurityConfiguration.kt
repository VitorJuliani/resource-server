package com.example.resourceserveropaque.configuration.http

import org.springframework.context.annotation.Bean
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.web.SecurityFilterChain

@EnableWebSecurity
class HttpSecurityConfiguration {

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        return http
            .authorizeRequests {
                it.mvcMatchers(HttpMethod.GET, "/api/v1/dogs/**").hasRole("USER")
                it.mvcMatchers(HttpMethod.GET, "/swagger-ui/**", "/v3/api-docs/**").permitAll()
                it.anyRequest().authenticated()
            }
            .oauth2ResourceServer { it.opaqueToken() }
            .build()
    }
}
