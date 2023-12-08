package cz.upce.bvwa2.plugins

import io.ktor.server.application.*

fun Application.configurePlugins() {
    configureCompression()
    configureContentNegotiation()
    configureResources()
    configureStatusPages()
    configureHttpsRedirect()
    configureCachingHeaders()
}