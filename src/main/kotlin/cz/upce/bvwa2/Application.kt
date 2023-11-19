package cz.upce.bvwa2

import cz.upce.bvwa2.auth.configureAuth
import cz.upce.bvwa2.plugins.configurePlugins
import cz.upce.bvwa2.routes.configureRoutes
import cz.upce.bvwa2.database.dao.DataFactory
import cz.upce.bvwa2.database.encryption.EncryptionFactory
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import org.slf4j.LoggerFactory
import java.security.KeyStore
import cz.upce.bvwa2.config as appConfig

fun main() {
    val environment = applicationEngineEnvironment {
        log = LoggerFactory.getLogger("ktor.application")
        developmentMode = appConfig.development

        val keyStore = KeyStore.getInstance(
            appConfig.server.ssl.keyStorePath,
            appConfig.server.ssl.keyStorePassword.toCharArray(),
        )

//        connector {
//            port = appConfig.server.port
//        }
        sslConnector(
            keyStore = keyStore,
            keyAlias = appConfig.server.ssl.keyAlias,
            keyStorePassword = { appConfig.server.ssl.keyStorePassword.toCharArray() },
            privateKeyPassword = { appConfig.server.ssl.privateKeyPassword.toCharArray() }) {
                port = appConfig.server.ssl.port
                keyStorePath = appConfig.server.ssl.keyStorePath
        }
        module(Application::module)
    }

    embeddedServer(Netty, environment).start(wait = true)
}

fun Application.module() {
    configurePlugins()
    configureAuth()
    configureRoutes()
    DataFactory.init()
    //EncryptionFactory.init()
}
