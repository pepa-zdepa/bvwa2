package cz.upce.bvwa2.plugins

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
    }
}