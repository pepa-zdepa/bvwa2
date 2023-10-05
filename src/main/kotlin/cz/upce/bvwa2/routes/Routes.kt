package cz.upce.bvwa2.routes

import cz.upce.bvwa2.auth.UserPrincipal
import cz.upce.bvwa2.routes.auth.authRoutes
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRoutes() {
    routing {
        authRoutes()

        get("/login") {
            call.respondText("/login")
        }

        authenticate("session") {
            get("/index") {
                val userSession = call.principal<UserPrincipal>()!!
                call.respondText("/index, Hello, ${userSession.remoteHost}")
            }
        }
    }
}