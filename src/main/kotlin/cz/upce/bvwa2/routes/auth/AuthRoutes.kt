package cz.upce.bvwa2.routes.auth

import cz.upce.bvwa2.auth.UserPrincipal
import cz.upce.bvwa2.repository.UserRepository
import io.ktor.http.*
import io.ktor.resources.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.resources.*
import io.ktor.server.resources.post
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import org.kodein.di.instance
import org.kodein.di.ktor.closestDI

@Resource("/auth")
class Auth {
    @Resource("/login")
    class Login(val parent: Auth = Auth())

    @Resource("/logout")
    class Logout(val parent: Auth = Auth())

    @Resource("/refresh")
    class Refresh(val parent: Auth = Auth())

    @Resource("/check-username-available")
    class CheckUsernameAvailable(val parent: Auth = Auth(), val username: String)
}

fun Route.authRoutes() {
    val sessionStorage by closestDI().instance<SessionStorage>()
    val userRepository by closestDI().instance<UserRepository>()

    authenticate("form") {
        post<Auth.Login> {
            call.sessions.set(call.principal<UserPrincipal>())

            call.respond(HttpStatusCode.OK)
        }
    }

    authenticate("session") {
        post<Auth.Logout> {
            call.sessions.clear<UserPrincipal>()
            sessionStorage.invalidate(call.sessionId!!)

            call.respond(HttpStatusCode.OK)
        }

        post<Auth.Refresh> {
            call.respond(HttpStatusCode.OK)
        }
    }

    // 0 - false, 1 - true
    get<Auth.CheckUsernameAvailable> {
        if (userRepository.doesUserExist(it.username))
            call.respond("0")
        else
            call.respond("1")
    }
}