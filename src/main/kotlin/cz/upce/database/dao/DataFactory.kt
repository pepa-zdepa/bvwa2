package cz.upce.database.dao

import cz.upce.database.table.Communications
import cz.upce.database.table.Roles
import cz.upce.database.table.Users
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction

object DataFactory {
    fun init() {
        val driverClassName = "org.sqlite.JDBC"
        val jdbcURL = "jdbc:sqlite:file:./build/db"
        val database = Database.connect(jdbcURL, driverClassName)

        transaction(database) {
            SchemaUtils.create(Communications)
            SchemaUtils.create(Users)
            SchemaUtils.create(Roles)
            Roles.insert {
                it[roleName] = "ADMIN"
            }
            Roles.insert {
                it[roleName] = "USER"
            }
        }
    }
}