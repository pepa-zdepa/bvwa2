package cz.upce.bvwa2.plugins

import io.ktor.server.application.*
import io.ktor.server.plugins.cachingheaders.*

fun Application.configureCachingHeaders() {
    install(CachingHeaders)
}