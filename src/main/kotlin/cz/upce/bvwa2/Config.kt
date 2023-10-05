package cz.upce.bvwa2

import com.sksamuel.hoplite.*
import com.sksamuel.hoplite.sources.SystemPropertiesPropertySource

data class Config(
    val development: Boolean,
    val server: Server,
    val auth: Auth,
) {
    data class Server(
        val port: Int,
    )
    data class Auth(
        val session: Session,
    ) {
        data class Session(
            val encryptKey: String,
            val signKey: String,
        )
    }
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