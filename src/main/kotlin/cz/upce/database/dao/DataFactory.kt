package cz.upce.database.dao

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

object DataFactory {
    fun init() {
        val driverClassName = "org.sqlite.JDBC"
        val jdbcURL = "jdbc:sqlite:file:./build/db"
        val database = Database.connect(jdbcURL, driverClassName)

        transaction(database) {
            //SchemaUtils
        }
    }
}