package cz.upce.bvwa2

import java.io.File

data class Config(
    val development: Boolean,
    val server: Server,
    val auth: Auth,
    val security: Security,
    val database: Database
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
        val loginServerUrl: String,
    ) {
        data class Session(
            val encryptKey: String,
            val signKey: String,
            val expirationInSeconds: Long,
        )
    }

    data class Security(
        val sqidsAlphabet: String,
        val databaseEncryptKey: String,
    )

    data class Database(
        val connectionString: String
    )
}
