package com.example.resourceserveropaque.configuration.token

import com.example.resourceserveropaque.configuration.token.introspector.CachedRedisTokenIntrospector
import com.example.resourceserveropaque.configuration.token.introspector.KeycloakRealmRoleAuthoritiesTokenIntrospector
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.security.oauth2.server.resource.introspection.NimbusOpaqueTokenIntrospector
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector

@Configuration
class TokenConfiguration(
    @Value("\${spring.security.oauth2.resourceserver.opaque.introspection-uri}")
    private val endpoint: String,
    @Value("\${spring.security.oauth2.resourceserver.opaque.introspection-client-id}")
    private val clientId: String,
    @Value("\${spring.security.oauth2.resourceserver.opaque.introspection-client-secret}")
    private val secretKey: String
) {

    @Bean
    @Primary
    fun cachedOpaqueTokenIntrospector(
        mapper: ObjectMapper,
        redisTemplate: RedisTemplate<String, String>,
        tokenProperties: TokenProperties,
        @Qualifier("OpaqueTokenIntrospector") delegate: OpaqueTokenIntrospector
    ): OpaqueTokenIntrospector =
        CachedRedisTokenIntrospector(redisTemplate, mapper, delegate, tokenProperties.cache.expiresInMilliseconds)

    @Bean
    @Qualifier(value = "OpaqueTokenIntrospector")
    fun opaqueTokenIntrospector(
        mapper: ObjectMapper
    ): OpaqueTokenIntrospector = KeycloakRealmRoleAuthoritiesTokenIntrospector(mapper, defaultOpaqueTokenIntrospector())

    private fun defaultOpaqueTokenIntrospector() =
        NimbusOpaqueTokenIntrospector(this.endpoint, this.clientId, this.secretKey)
}
