package cz.upce.bvwa2.auth

import cz.upce.bvwa2.SessionRepo
import cz.upce.bvwa2.config
import io.github.omkartenkale.ktor_role_based_auth.roleBased
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.plugins.*
import io.ktor.server.response.*
import io.ktor.server.sessions.*

data class UserPrincipal(
    val userId: Long, // TODO get from DB
    val remoteHost: String,
    val expiration: Long,
    val roles: Set<String> = setOf("user"),
) : Principal

// TODO database storage
//val sessionStorage = directorySessionStorage(File("build/.sessions"), true) // bez transform(...) vyhodí "sessionId not set" po logout
val sessionStorage = object : SessionStorage {
    val repo = SessionRepo()

    override suspend fun invalidate(id: String) = repo.delete(id)

    override suspend fun read(id: String) = repo.getById(id)?.data ?: throw NoSuchElementException("Session $id not found")

    override suspend fun write(id: String, value: String) = repo.add(id, value)

}
private fun getExpiration() = System.currentTimeMillis() + config.auth.session.expirationInSeconds * 1000
private fun validateExpiration(expiration: Long) = expiration > System.currentTimeMillis()

fun Application.configureAuth() {
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
                    UserPrincipal(0, request.origin.remoteHost, getExpiration())
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