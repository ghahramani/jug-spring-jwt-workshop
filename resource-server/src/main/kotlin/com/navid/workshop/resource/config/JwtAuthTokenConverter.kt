package com.navid.workshop.resource.config

import org.springframework.core.convert.converter.Converter
import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class JwtAuthTokenConverter : Converter<Jwt, Mono<AbstractAuthenticationToken>> {

    override fun convert(source: Jwt): Mono<AbstractAuthenticationToken> = Mono.fromCallable {
        val converter = JwtAuthenticationConverter()
        converter.setJwtGrantedAuthoritiesConverter { jwt ->
            val roles = jwt.getClaimAsStringList("roles")
            roles.map { name -> SimpleGrantedAuthority("ROLE_$name") }
        }
        converter.convert(source)
    }

}
