package cz.upce.bvwa2

import cz.upce.bvwa2.auth.configureAuth
import cz.upce.bvwa2.plugins.configurePlugins
import cz.upce.bvwa2.routes.configureRoutes
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

fun main() {
    if (config.development) System.setProperty("io.ktor.development", "true")

    embeddedServer(
        Netty,
        port = config.server.port,
        host = "0.0.0.0",
        module = Application::module,
    ).start(wait = true)
}

fun Application.module() {
    configurePlugins()
    configureAuth()
    configureRoutes()
}
