package com.navid.workshop.auth.server

import com.navid.workshop.auth.server.domain.util.ClientScope.*
import com.navid.workshop.auth.server.domain.util.UserRole
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.oauth2.core.AuthorizationGrantType
import org.springframework.security.oauth2.core.ClientAuthenticationMethod
import org.springframework.security.oauth2.core.oidc.OidcScopes
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository
import org.springframework.security.provisioning.UserDetailsManager
import java.util.*


@SpringBootApplication
class AuthServerApplication {

    @Bean
    fun init(
        repository: RegisteredClientRepository,
        userDetailManager: UserDetailsManager
    ): CommandLineRunner {
        return CommandLineRunner { args: Array<String?>? ->
            val client = RegisteredClient
                .withId(UUID.randomUUID().toString())
                .clientId("navid")
                .clientSecret("{noop}navid-pass")
                .redirectUri("https://auth-client.loca.lt/login/oauth2/code/navid-oidc")
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
                .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
                .scope(ACTUATOR_READ.name)
                .scope(ENDPOINT_READ.name)
                .scope(ENDPOINT_WRITE.name)
                .scope(ENDPOINT_DELETE.name)
                .scope(OidcScopes.OPENID)
                .scope(OidcScopes.PROFILE)
                .scope(OidcScopes.EMAIL)
                .build()
            repository.save(client)

            val actuator = User.builder()
                .username("actuator")
                .password("{noop}password")
                .roles(UserRole.ACTUATOR.name)
                .build()
            val admin = User.builder()
                .username("admin")
                .password("{noop}password")
                .roles(UserRole.ENDPOINT_ADMIN.name, UserRole.ENDPOINT_USER.name)
                .build()
            val user: UserDetails? = User.builder()
                .username("user")
                .password("{noop}password")
                .roles(UserRole.ENDPOINT_USER.name)
                .build()
            userDetailManager.createUser(actuator)
            userDetailManager.createUser(admin)
            userDetailManager.createUser(user)
        }
    }

}

fun main(args: Array<String>) {
    runApplication<AuthServerApplication>(*args)
}