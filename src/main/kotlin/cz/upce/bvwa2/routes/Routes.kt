package cz.upce.bvwa2.routes

import cz.upce.bvwa2.routes.admin.adminRoutes
import cz.upce.bvwa2.routes.auth.authRoutes
import cz.upce.bvwa2.routes.user.userRoutes
import io.github.omkartenkale.ktor_role_based_auth.withRole
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.http.content.*
import io.ktor.server.routing.*

fun Application.configureRoutes() {
    routing {
//        singlePageApplication {
//            react("frontend")
//        }

        authRoutes()
        userRoutes()

        authenticate("session") {
            withRole("admin") {
                adminRoutes()
            }
        }
    }
}