package com.example.resourceserveropaque.configuration.token

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConfigurationProperties(prefix = "token")
@ConstructorBinding
data class TokenProperties(
    val cache: CacheProperties
) {
    data class CacheProperties(
        val expiresInMilliseconds: Long
    )
}
