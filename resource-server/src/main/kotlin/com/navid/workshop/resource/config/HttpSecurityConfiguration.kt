package com.navid.workshop.resource.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.config.web.server.invoke
import org.springframework.security.web.server.SecurityWebFilterChain

@Configuration
class HttpSecurityConfiguration {

    @Bean
    fun securityFilterChain(
        http: ServerHttpSecurity,
        converter: JwtAuthTokenConverter,
    ): SecurityWebFilterChain = http {
        authorizeExchange {
            authorize("/api/admin", hasRole("ROLE_ENDPOINT_ADMIN"))
            authorize("/api/user", hasAnyRole("ROLE_ENDPOINT_USER", "ROLE_ENDPOINT_ADMIN"))
            authorize(anyExchange, authenticated)
        }
        oauth2ResourceServer {
            jwt {
                jwtAuthenticationConverter = converter
            }
        }
    }

}