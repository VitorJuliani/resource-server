package com.example.resourceserveropaque.security.http

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
                it.mvcMatchers(HttpMethod.GET, "/api/v1/greeting").hasRole("USER")
                it.anyRequest().authenticated()
            }
            .oauth2ResourceServer { it.opaqueToken() }
            .build()
    }
}