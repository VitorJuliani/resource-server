package com.example.resourceserveropaque.configuration.token.introspector

import com.example.resourceserveropaque.configuration.token.const.KeycloakTokenAttributresClaimNames.EXT
import com.example.resourceserveropaque.configuration.token.const.KeycloakTokenAttributresClaimNames.IAT
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.core.DefaultOAuth2AuthenticatedPrincipal
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector
import java.time.Instant
import java.util.concurrent.TimeUnit

class CachedRedisTokenIntrospector(
    private val template: RedisTemplate<String, String>,
    private val mapper: ObjectMapper,
    private val delegate: OpaqueTokenIntrospector,
    private val expiresInMilliseconds: Long
) : OpaqueTokenIntrospector {

    override fun introspect(token: String): OAuth2AuthenticatedPrincipal {
        return this.template.opsForValue().get(token)
            ?.let { this.mapper.readValue(it, CachedOAuth2AuthenticatedPrincipal::class.java) }
            ?.toOAuth2AuthenticatedPrincipal()
            ?: executeAndSave(token)
    }

    private fun executeAndSave(token: String): OAuth2AuthenticatedPrincipal {
        return this.delegate.introspect(token)
            .also {
                this.template.opsForValue().set(
                    token,
                    this.mapper.writeValueAsString(it),
                    this.expiresInMilliseconds.minus(1),
                    TimeUnit.MILLISECONDS
                )
            }
    }

    private data class CachedOAuth2AuthenticatedPrincipal(
        val name: String,
        val authorities: Collection<Map<String, String>>,
        val attributes: Map<String, Any>
    ) {
        fun toOAuth2AuthenticatedPrincipal(): OAuth2AuthenticatedPrincipal =
            DefaultOAuth2AuthenticatedPrincipal(
                this.name,
                replaceDateAttributesToInstant(),
                authoritiesAsGrantedAuthorities()
            )

        private fun authoritiesAsGrantedAuthorities(): Collection<GrantedAuthority> {
            return mutableListOf<GrantedAuthority>()
                .apply {
                    authorities.forEach {
                        it.values.forEach { authority ->
                            this.add(SimpleGrantedAuthority(authority))
                        }
                    }
                }
        }

        private fun replaceDateAttributesToInstant(): Map<String, Any> {
            return this.attributes.toMutableMap()
                .apply {
                    this[EXT] = Instant.parse(this[EXT].toString())
                    this[IAT] = Instant.parse(this[IAT].toString())
                }
        }
    }
}
