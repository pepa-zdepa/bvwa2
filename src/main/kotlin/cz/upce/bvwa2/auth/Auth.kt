package cz.upce.bvwa2.auth

import cz.upce.bvwa2.config
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.plugins.*
import io.ktor.server.response.*
import io.ktor.server.sessions.*
import io.ktor.util.*
import java.io.File

data class UserPrincipal(val remoteHost: String, val roles: List<String> = listOf()) : Principal

// TODO database storage
val storage = directorySessionStorage(File("build/.sessions"), true) // bez transform(...) vyhodí "sessionId not set" po logout

fun Application.configureAuth() {
    install(Sessions) {
        cookie<UserPrincipal>("user_session", storage) {
            cookie.path = "/"
            cookie.maxAgeInSeconds = 60
            cookie.secure = false // TODO true
            cookie.httpOnly = true
//            cookie.extensions["SameSite"] = "lax" // TODO
            transform(SessionTransportTransformerEncrypt(hex(config.auth.session.encryptKey), hex(config.auth.session.signKey)))
        }
    }

    authentication {
        form("form") {
            userParamName = "user"
            passwordParamName = "password"

            validate {credentials ->
                // TODO get data from DB
                if (credentials.name == "user" && credentials.password == "pass") {
                    UserPrincipal(request.origin.remoteHost)
                } else {
                    null
                }
            }

            challenge {
                call.respond(HttpStatusCode.Unauthorized, "Invalid credentials")
            }
        }

        session<UserPrincipal>("session") {
            validate { session ->
                if (request.origin.remoteHost != session.remoteHost) return@validate null

                sessions.set(UserPrincipal(session.remoteHost))
                session
            }

            challenge {
                call.respond(HttpStatusCode.Unauthorized, "Invalid credentials")
            }
        }
    }
}