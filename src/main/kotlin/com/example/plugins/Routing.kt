package com.example.plugins

import com.example.routes.notesRoute
import com.example.routes.userAuthenticRoute
import io.ktor.server.routing.*
import io.ktor.server.response.*
import io.ktor.server.application.*

fun Application.configureRouting() {
    routing {
        notesRoute()
        userAuthenticRoute()
    }

}
