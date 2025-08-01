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
            authorize("/api/admin", hasRole("ENDPOINT_ADMIN"))
            authorize("/api/user", hasAnyRole("ENDPOINT_USER", "ENDPOINT_ADMIN"))
            authorize("/actuator/health", permitAll)
            authorize(anyExchange, authenticated)
        }
        oauth2ResourceServer {
            jwt {
                jwtAuthenticationConverter = converter
            }
        }
    }

}