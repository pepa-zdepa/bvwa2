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
        anyHost()
//        allowHost("127.0.0.1:5500")
        allowSameOrigin = true
        allowNonSimpleContentTypes = true

//        allowHeader("user_session")
//        exposeHeader("user_session")

        allowMethod(HttpMethod.Get)
        allowMethod(HttpMethod.Post)
        allowMethod(HttpMethod.Options)
        allowMethod(HttpMethod.Put)
        allowMethod(HttpMethod.Delete)
        allowMethod(HttpMethod.Patch)
        allowHeader(HttpHeaders.Authorization)
    }
}