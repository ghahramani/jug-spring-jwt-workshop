package com.navid.workshop.resource.resource

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api")
class ApiResource {

    @GetMapping("/admin")
    fun admin() = "Hello Admin"

    @GetMapping("/user")
    fun user() = "Hello User"
}