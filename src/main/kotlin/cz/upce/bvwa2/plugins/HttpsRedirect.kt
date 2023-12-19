package cz.upce.bvwa2.plugins

import cz.upce.bvwa2.Config
import io.ktor.server.application.*
import org.kodein.di.instance
import org.kodein.di.ktor.closestDI

fun Application.configureHttpsRedirect() {
    val config by closestDI().instance<Config>()

//    install(HttpsRedirect) {
//        sslPort = config.server.ssl.port
//        permanentRedirect = true
//    }
}