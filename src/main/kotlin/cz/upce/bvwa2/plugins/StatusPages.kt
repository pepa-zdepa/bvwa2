package cz.upce.bvwa2.plugins

import cz.upce.bvwa2.database.PersistenceException
import io.github.omkartenkale.ktor_role_based_auth.UnauthorizedAccessException
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import io.ktor.utils.io.*

fun Application.configureStatusPages() {
    install(StatusPages) {
        exception<Throwable> { call, cause ->
            cause.printStack()
            call.respondText(text = "500: $cause" , status = HttpStatusCode.InternalServerError)
        }

        exception<UnauthorizedAccessException> { call, cause ->
            call.respondText(text = "Unauthorized" , status = HttpStatusCode.Forbidden)
        }
        exception<PersistenceException> { call, cause ->
            call.respond(HttpStatusCode.BadRequest)
        }
    }
}