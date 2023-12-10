package cz.upce.bvwa2.plugins

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.cors.routing.*

fun Application.configurePlugins() {
    configureCompression()
    configureContentNegotiation()
    configureResources()
    configureStatusPages()
    configureHttpsRedirect()
    configureCachingHeaders()
    configureRequestValidation()

    install(CORS) {
        allowCredentials = true

        allowMethod(HttpMethod.Get)
        allowMethod(HttpMethod.Post)
        allowMethod(HttpMethod.Options)
        allowMethod(HttpMethod.Put)
        allowMethod(HttpMethod.Delete)
        allowMethod(HttpMethod.Patch)
        allowHeader(HttpHeaders.Authorization)
    }
}