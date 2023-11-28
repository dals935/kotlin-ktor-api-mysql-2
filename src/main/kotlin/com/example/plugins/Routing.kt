package com.example.plugins

import com.example.routes.personRouting
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        personRouting()
    }
}
