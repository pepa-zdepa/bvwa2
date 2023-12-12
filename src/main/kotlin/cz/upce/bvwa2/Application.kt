package cz.upce.bvwa2

import com.sksamuel.hoplite.*
import com.sksamuel.hoplite.sources.SystemPropertiesPropertySource
import cz.upce.bvwa2.auth.configureAuth
import cz.upce.bvwa2.database.DatabaseConnection
import cz.upce.bvwa2.database.dao.*
import cz.upce.bvwa2.database.encryption.Encryption
import cz.upce.bvwa2.plugins.configurePlugins
import cz.upce.bvwa2.repository.MessageRepository
import cz.upce.bvwa2.repository.SessionRepository
import cz.upce.bvwa2.repository.UserRepository
import cz.upce.bvwa2.routes.configureRoutes
import cz.upce.bvwa2.utils.IdConverter
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.sessions.*
import org.kodein.di.*
import org.kodein.di.ktor.closestDI
import org.kodein.di.ktor.di
import org.slf4j.LoggerFactory
import java.security.KeyStore

private val overrideConfig: String? = System.getProperty("config", null)

@OptIn(ExperimentalHoplite::class)
private val appConfig = ConfigLoaderBuilder.newBuilder()
    .addSource(SystemPropertiesPropertySource())
    .addEnvironmentSource()
    .let {
        if (overrideConfig != null) it.addFileSource(overrideConfig)
        else it
    }
    .addResourceSource("/application-default.conf")
    .build()
    .loadConfigOrThrow<Config>()

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
    di {
        bindInstance { appConfig }

        bindProviderOf(::Encryption)
        bindSingleton<IdConverter> { IdConverter(instance<Config>().security.sqidsAlphabet) }

        bindProvider<IGenderDao> { new(::GenderDao) }
        bindProvider<IMessagesDao> { new(::MessagesDao) }
        bindProvider<IRoleDao> { new(::RoleDao) }
        bindProvider<ISessionDao> { new(::SessionDao) }
        bindProvider<IUserDao> { new(::UserDao) }

        bindSingletonOf(::DatabaseConnection)

        bindProviderOf(::SessionRepository)
        bindProviderOf(::UserRepository)
        bindProviderOf(::MessageRepository)

        bindSingleton<SessionStorage> {
            object : SessionStorage {
                private val sessionRepository = instance<SessionRepository>()

                override suspend fun invalidate(id: String) = sessionRepository.delete(id)

                override suspend fun read(id: String) =
                    sessionRepository.getById(id)?.data ?: throw NoSuchElementException("Session $id not found")

                override suspend fun write(id: String, value: String) = sessionRepository.add(id, value)
            }
        }
    }

    val databaseConnection by closestDI().instance<DatabaseConnection>()
    databaseConnection.connect()
    databaseConnection.init()

    configurePlugins()
    configureAuth()
    configureRoutes()
    //EncryptionFactory.init()
}
