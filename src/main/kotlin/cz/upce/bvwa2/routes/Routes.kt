package cz.upce.bvwa2.routes

import cz.upce.bvwa2.auth.UserPrincipal
import cz.upce.bvwa2.routes.admin.adminRoutes
import cz.upce.bvwa2.routes.auth.authRoutes
import cz.upce.bvwa2.routes.user.userRoutes
import io.github.omkartenkale.ktor_role_based_auth.withRole
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRoutes() {
    routing {
        authRoutes()
        userRoutes()
        adminRoutes()

        authenticate("session") {
            withRole("user") {
                get("/index") {
                    val userSession = call.principal<UserPrincipal>()!!
                    call.respondText("/index, Hello, ${userSession.remoteHost}")
                }
            }

            withRole("admin") {
                get("/admin") {
                    val userSession = call.principal<UserPrincipal>()!!
                    call.respondText("/index, Hello, ${userSession.remoteHost} as admin")
                }
            }
        }
    }
}