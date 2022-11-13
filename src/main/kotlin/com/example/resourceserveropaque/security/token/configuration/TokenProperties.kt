package com.example.resourceserveropaque.security.token.configuration

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConfigurationProperties(prefix = "token")
@ConstructorBinding
data class TokenProperties(
    val cache: CacheProperties
) {
    data class CacheProperties(
        val expiresInSeconds: Long
    )
}
