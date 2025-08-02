package com.navid.workshop.auth.client.controller

import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.core.oidc.user.OidcUser
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.reactive.result.view.Rendering
import reactor.core.publisher.Mono

@Controller
class IndexController {

    @GetMapping("/")
    fun index(@AuthenticationPrincipal oidcUser: OidcUser?): Mono<Rendering> = Mono.fromCallable {
        Rendering
            .view("index")
            .modelAttribute("oidcUser", oidcUser ?: "")
            .build()
    }

}