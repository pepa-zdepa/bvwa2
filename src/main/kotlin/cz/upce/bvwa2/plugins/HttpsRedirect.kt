package cz.upce.bvwa2.plugins

import cz.upce.bvwa2.config
import io.ktor.server.application.*
import io.ktor.server.plugins.httpsredirect.*

fun Application.configureHttpsRedirect() {
    install(HttpsRedirect) {
        sslPort = config.server.ssl.port
        permanentRedirect = true
    }
}