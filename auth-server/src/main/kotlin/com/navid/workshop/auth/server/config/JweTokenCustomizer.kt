package com.navid.workshop.auth.server.config

import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer
import org.springframework.stereotype.Component

@Component
class JweTokenCustomizer : OAuth2TokenCustomizer<JwtEncodingContext> {
    override fun customize(context: JwtEncodingContext) {
        val principal = context.getPrincipal<Authentication>()
        val authorities = principal.authorities
            .map { obj: GrantedAuthority -> obj.authority }
            .toHashSet()

        context.claims.claim("roles", authorities)
        context.claims.claim("email", "${principal.name}@jug.ir")
    }
}