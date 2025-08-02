package com.navid.workshop.auth.server.config

import com.nimbusds.jose.jwk.source.JWKSource
import com.nimbusds.jose.proc.SecurityContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.Order
import org.springframework.http.MediaType
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.factory.PasswordEncoderFactories
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.oauth2.jwt.JwtDecoder
import org.springframework.security.oauth2.server.authorization.JdbcOAuth2AuthorizationService
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService
import org.springframework.security.oauth2.server.authorization.client.JdbcRegisteredClientRepository
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer
import org.springframework.security.provisioning.JdbcUserDetailsManager
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint
import org.springframework.security.web.util.matcher.MediaTypeRequestMatcher
import javax.sql.DataSource


@Configuration(proxyBeanMethods = false)
class HttpSecurityConfiguration {

    @Bean
    @Order(1)
    fun securityFilterChain(
        http: HttpSecurity
    ): SecurityFilterChain = run {
        val authorizationServer = OAuth2AuthorizationServerConfigurer.authorizationServer()
        http
            .securityMatcher(authorizationServer.endpointsMatcher)
            .with(authorizationServer) { authorizationServer ->
                authorizationServer.oidc(Customizer.withDefaults())
            }
            .authorizeHttpRequests { authorizeRequests ->
                authorizeRequests
                    .anyRequest()
                    .authenticated()
            }
            .exceptionHandling { exceptions ->
                exceptions
                    .defaultAuthenticationEntryPointFor(
                        LoginUrlAuthenticationEntryPoint("/login"),
                        MediaTypeRequestMatcher(MediaType.TEXT_HTML)
                    )
            }
            .build()
    }

    @Bean
    fun jwtDecoder(jwkSource: JWKSource<SecurityContext>): JwtDecoder {
        return OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource)
    }

    @Bean
    @Order(2)
    fun defaultSecurityFilterChain(
        http: HttpSecurity
    ): SecurityFilterChain = run {
        http
            .authorizeHttpRequests { authorize ->
                authorize
                    .anyRequest()
                    .authenticated()
            }
            .formLogin(Customizer.withDefaults())
            .build()
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder()

    @Bean
    fun registeredClientRepository(
        operation: JdbcTemplate
    ): RegisteredClientRepository = JdbcRegisteredClientRepository(operation)

    @Bean
    fun authorizationService(
        jdbcTemplate: JdbcTemplate,
        repository: RegisteredClientRepository
    ): OAuth2AuthorizationService = JdbcOAuth2AuthorizationService(
        jdbcTemplate,
        repository
    )

    @Bean
    fun userDetailsService(
        dataSource: DataSource
    ): UserDetailsService = JdbcUserDetailsManager(dataSource)

}