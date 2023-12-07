package cz.upce.bvwa2.routes.auth

import cz.upce.bvwa2.auth.UserPrincipal
import io.ktor.http.*
import io.ktor.resources.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.resources.post
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import org.kodein.di.instance
import org.kodein.di.ktor.closestDI

@Resource("/login")
class Login

@Resource("/logout")
class Logout

@Resource("/refresh")
class Refresh

fun Route.authRoutes() {
    val sessionStorage by closestDI().instance<SessionStorage>()

    route("/auth") {
        authenticate("form") {
            post<Login> {
                call.sessions.set(call.principal<UserPrincipal>())

                call.respond(HttpStatusCode.OK)
            }
        }

        authenticate("session") {
            post<Logout> {
                call.sessions.clear<UserPrincipal>()
                sessionStorage.invalidate(call.sessionId!!)

                call.respond(HttpStatusCode.OK)
            }

            post<Refresh> {
                call.respond(HttpStatusCode.OK)
            }
        }
    }
}