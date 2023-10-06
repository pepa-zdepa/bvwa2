package cz.upce

import cz.upce.database.dao.DataFactory
import cz.upce.plugins.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    configureSockets()
    configureSerialization()
    configureDatabases()
    configureMonitoring()
    configureHTTP()
    configureSecurity()
    configureRouting()
    DataFactory.init()
}
