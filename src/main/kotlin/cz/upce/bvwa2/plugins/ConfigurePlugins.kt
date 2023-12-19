package cz.upce.bvwa2.plugins

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.application.ApplicationCallPipeline.ApplicationPhase.Plugins
import io.ktor.server.plugins.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.plugins.forwardedheaders.*

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
        anyHost()
        allowSameOrigin = true
        allowNonSimpleContentTypes = true

        allowMethod(HttpMethod.Get)
        allowMethod(HttpMethod.Post)
        allowMethod(HttpMethod.Options)
        allowMethod(HttpMethod.Put)
        allowMethod(HttpMethod.Delete)
        allowMethod(HttpMethod.Patch)
        allowHeader(HttpHeaders.Authorization)
    }

    install(ForwardedHeaders)

    intercept(Plugins) {
        call.mutableOriginConnectionPoint.scheme = "https"
    }
}