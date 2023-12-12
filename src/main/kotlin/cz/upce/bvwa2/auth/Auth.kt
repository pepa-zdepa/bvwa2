package cz.upce.bvwa2.auth

import cz.upce.bvwa2.Config
import cz.upce.bvwa2.models.LoginRequest
import cz.upce.bvwa2.repository.UserRepository
import io.github.omkartenkale.ktor_role_based_auth.roleBased
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.forms.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.plugins.*
import io.ktor.server.response.*
import io.ktor.server.sessions.*
import kotlinx.datetime.Clock
import org.kodein.di.instance
import org.kodein.di.ktor.closestDI


data class UserPrincipal(
    val userId: Long,
    val username: String,
    val remoteHost: String,
    val expiration: Long,
    val role: String,
) : Principal

private fun validateExpiration(expiration: Long) = expiration > System.currentTimeMillis()

fun Application.configureAuth() {
    val userRepository by closestDI().instance<UserRepository>()
    val config by closestDI().instance<Config>()
    val sessionStorage by closestDI().instance<SessionStorage>()

    val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json()
        }
    }

    fun getExpiration() = Clock.System.now().toEpochMilliseconds() + config.auth.session.expirationInSeconds * 1000

    install(Sessions) {
        cookie<UserPrincipal>("user_session", sessionStorage) {
            cookie.path = "/"
            cookie.maxAgeInSeconds = config.auth.session.expirationInSeconds
            cookie.secure = true
            cookie.httpOnly = false
            cookie.extensions["SameSite"] = "none"
//            transform(SessionTransportTransformerEncrypt(hex(config.auth.session.encryptKey), hex(config.auth.session.signKey)))
        }
    }

    authentication {
        form("form") {
            userParamName = "user"
            passwordParamName = "password"

            validate {credentials ->
                val response = client.submitForm(
                    url = config.auth.loginServerUrl,
                    formParameters = parameters {
                        append("user", credentials.name)
                        append("password", credentials.password)
                    }
                )

                if (response.status != HttpStatusCode.OK)
                    return@validate null

                val body = response.body<LoginRequest>()
                UserPrincipal(body.id, body.nickname, request.origin.remoteHost, getExpiration(), body.role.lowercase())
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
                setOf((it as UserPrincipal).role)
            }
            throwErrorOnUnauthorizedResponse = true
        }
    }
}