package cz.upce.bvwa2.plugins

import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*

fun Application.configureContentNegotiation() {
    install(ContentNegotiation) {
        json()
    }
}