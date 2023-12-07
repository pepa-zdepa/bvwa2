package cz.upce.bvwa2

import com.sksamuel.hoplite.*
import com.sksamuel.hoplite.sources.SystemPropertiesPropertySource
import java.io.File

data class Config(
    val development: Boolean,
    val server: Server,
    val auth: Auth,
    val encryptKey: EncryptKey,
) {
    data class Server(
        val port: Int,
        val ssl: Ssl,
    ) {
        data class Ssl(
            val keyAlias: String,
            val keyStorePassword: String,
            val privateKeyPassword: String,
            val port: Int,
            val keyStorePath: File,
        )
    }
    data class Auth(
        val session: Session,
    ) {
        data class Session(
            val encryptKey: String,
            val signKey: String,
            val expirationInSeconds: Long,
        )
    }

    data class EncryptKey(
        val key: String,
    )

}

private val overrideConfig: String? = System.getProperty("config", null)

@OptIn(ExperimentalHoplite::class)
val config = ConfigLoaderBuilder.newBuilder()
    .addSource(SystemPropertiesPropertySource())
    .addEnvironmentSource()
    .let {
        if (overrideConfig != null) it.addFileSource(overrideConfig)
        else it
    }
    .addResourceSource("/application-default.conf")
    .build()
    .loadConfigOrThrow<Config>()