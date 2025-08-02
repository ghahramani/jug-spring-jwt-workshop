package com.navid.workshop.auth.client.controller

import org.springframework.http.ResponseEntity
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.reactive.function.client.WebClient

@RestController
class WorldResource(private val webClient: WebClient) {

    @GetMapping("/admin")
    fun admin(
        @RegisteredOAuth2AuthorizedClient
        authorizedClient: OAuth2AuthorizedClient
    ) = webClient
        .get()
        .uri("https://auth-user.loca.lt/api/admin")
        .attributes { attrs ->
            attrs[OAuth2AuthorizedClient::class.java.name] = authorizedClient
        }
        .exchangeToMono { clientResponse ->
            clientResponse.toEntity(String::class.java)
        }

    @GetMapping("/user")
    fun user(
        @RegisteredOAuth2AuthorizedClient
        authorizedClient: OAuth2AuthorizedClient
    ) = webClient
        .get()
        .uri("https://auth-user.loca.lt/api/user")
        .attributes { attrs ->
            attrs[OAuth2AuthorizedClient::class.java.name] = authorizedClient
        }
        .exchangeToMono { clientResponse ->
            clientResponse.toEntity(String::class.java)
        }

    @GetMapping("/monitoring")
    fun actuator(
        @RegisteredOAuth2AuthorizedClient
        authorizedClient: OAuth2AuthorizedClient
    ) = webClient
        .get()
        .uri("https://auth-user.loca.lt/actuator/health")
        .attributes { attrs ->
            attrs[OAuth2AuthorizedClient::class.java.name] = authorizedClient
        }
        .exchangeToMono { clientResponse ->
            clientResponse.toEntity(String::class.java)
        }

    @GetMapping("/me")
    fun me(
        @RegisteredOAuth2AuthorizedClient("navid-oidc")
        authorizedClient: OAuth2AuthorizedClient
    ): ResponseEntity<Any> {
        return ResponseEntity.ok(authorizedClient)
    }

}