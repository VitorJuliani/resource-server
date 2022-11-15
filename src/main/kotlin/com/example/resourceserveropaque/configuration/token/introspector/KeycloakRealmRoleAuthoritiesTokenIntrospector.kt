package com.example.resourceserveropaque.configuration.token.introspector

import com.example.resourceserveropaque.configuration.token.const.KeycloakTokenAttributresClaimNames.REALM_ACCESS
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.core.DefaultOAuth2AuthenticatedPrincipal
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector

class KeycloakRealmRoleAuthoritiesTokenIntrospector(
    private val mapper: ObjectMapper,
    private val delegate: OpaqueTokenIntrospector
) : OpaqueTokenIntrospector {
    override fun introspect(token: String): OAuth2AuthenticatedPrincipal {
        return this.delegate.introspect(token)
            .let {
                val authorities = it.authorities + it.realmAccessAuthorities()
                DefaultOAuth2AuthenticatedPrincipal(it.attributes, authorities)
            }
    }

    private fun OAuth2AuthenticatedPrincipal.realmAccessAuthorities(): Collection<GrantedAuthority> {
        return this.attributes[REALM_ACCESS]
            ?.toString()
            ?.let { mapper.readValue(it, RealmAccess::class.java) }
            ?.roles
            ?.map { "ROLE_$it" }
            ?.map(::SimpleGrantedAuthority)
            ?: emptyList()
    }

    private data class RealmAccess(val roles: List<String>)
}
