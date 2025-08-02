package com.navid.workshop.auth.client.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.config.web.server.invoke
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientManager
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientProviderBuilder
import org.springframework.security.oauth2.client.oidc.web.server.logout.OidcClientInitiatedServerLogoutSuccessHandler
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository
import org.springframework.security.oauth2.client.web.DefaultReactiveOAuth2AuthorizedClientManager
import org.springframework.security.oauth2.client.web.reactive.function.client.ServerOAuth2AuthorizedClientExchangeFilterFunction
import org.springframework.security.oauth2.client.web.server.ServerOAuth2AuthorizedClientRepository
import org.springframework.security.web.server.SecurityWebFilterChain
import org.springframework.web.reactive.function.client.WebClient

@Configuration
@EnableWebFluxSecurity
class HttpSecurityConfiguration {

    @Bean
    fun webClient(
        builder: WebClient.Builder,
        clientManager: ReactiveOAuth2AuthorizedClientManager
    ) = builder
        .filter(ServerOAuth2AuthorizedClientExchangeFilterFunction(clientManager))
        .build()

    @Bean
    fun clientManager(
        clients: ReactiveClientRegistrationRepository,
        authzRepo: ServerOAuth2AuthorizedClientRepository
    ): ReactiveOAuth2AuthorizedClientManager = DefaultReactiveOAuth2AuthorizedClientManager(
        clients,
        authzRepo
    ).apply {
        val provider = ReactiveOAuth2AuthorizedClientProviderBuilder
            .builder()
            .authorizationCode()
            .refreshToken()
            .clientCredentials()
            .build()
        setAuthorizedClientProvider(provider)
    }

    @Bean
    fun springSecurityFilterChain(
        http: ServerHttpSecurity
    ): SecurityWebFilterChain = http {
        authorizeExchange {
            authorize("/login", permitAll)
            authorize("/error", permitAll)
            authorize(anyExchange, authenticated)
        }
        oauth2Login { }
    }

    @Bean
    fun logoutSucHandler(registry: ReactiveClientRegistrationRepository) =
        OidcClientInitiatedServerLogoutSuccessHandler(registry)

}