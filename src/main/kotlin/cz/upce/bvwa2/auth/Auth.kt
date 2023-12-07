package cz.upce.bvwa2.auth

import cz.upce.bvwa2.Config
import cz.upce.bvwa2.repository.SessionRepository
import cz.upce.bvwa2.repository.UserRepository
import io.github.omkartenkale.ktor_role_based_auth.roleBased
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.plugins.*
import io.ktor.server.response.*
import io.ktor.server.sessions.*
import org.kodein.di.instance
import org.kodein.di.ktor.closestDI

data class UserPrincipal(
    val userId: Long, // TODO get from DB
    val username: String,
    val remoteHost: String,
    val expiration: Long,
    val roles: Set<String> = setOf("user"),
) : Principal

private fun validateExpiration(expiration: Long) = expiration > System.currentTimeMillis()

fun Application.configureAuth() {
    val userRepository by closestDI().instance<UserRepository>()
    val config by closestDI().instance<Config>()
    val sessionStorage by closestDI().instance<SessionStorage>()

    fun getExpiration() = System.currentTimeMillis() + config.auth.session.expirationInSeconds * 1000

    install(Sessions) {
        cookie<UserPrincipal>("user_session", sessionStorage) {
            cookie.path = "/"
            cookie.maxAgeInSeconds = config.auth.session.expirationInSeconds
            cookie.secure = true
            cookie.httpOnly = true
            cookie.extensions["SameSite"] = "lax"
//            transform(SessionTransportTransformerEncrypt(hex(config.auth.session.encryptKey), hex(config.auth.session.signKey)))
        }
    }

    authentication {
        form("form") {
            userParamName = "user"
            passwordParamName = "password"

            validate {credentials ->
                // TODO get data from DB
                if (credentials.name == "user" && credentials.password == "pass") {
                    UserPrincipal(0, "user", request.origin.remoteHost, getExpiration())
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
                if (request.origin.remoteHost != session.remoteHost || !validateExpiration(session.expiration)) {
                    sessionStorage.invalidate(sessionId!!)
                    return@validate null
                }

                val newSession = session.copy(expiration = getExpiration())
                sessions.set(newSession)
                newSession
            }

            challenge {
                call.respond(HttpStatusCode.Unauthorized, "Invalid credentials")
            }
        }

        roleBased {
            extractRoles {
                (it as UserPrincipal).roles
            }
            throwErrorOnUnauthorizedResponse = true
        }
    }
}