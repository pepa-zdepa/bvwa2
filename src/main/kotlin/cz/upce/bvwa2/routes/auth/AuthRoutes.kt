package cz.upce.bvwa2.routes.auth

import cz.upce.bvwa2.auth.UserPrincipal
import cz.upce.bvwa2.auth.sessionStorage
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.resources.post
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*

fun Route.authRoutes() {
    route("/auth") {
        authenticate("form") {
            post<Login> {
                call.sessions.set(call.principal<UserPrincipal>())

                call.respondRedirect("/index")
            }
        }

        authenticate("session") {
            post<Logout> {
                call.sessions.clear<UserPrincipal>()
                sessionStorage.invalidate(call.sessionId!!)

                call.respondRedirect("/login")
            }
        }
    }
}