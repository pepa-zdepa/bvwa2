package cz.upce.bvwa2.database.dao

import cz.upce.bvwa2.database.model.Communication
import cz.upce.bvwa2.database.table.Communications
import cz.upce.bvwa2.database.table.Roles
import cz.upce.bvwa2.database.table.Sessions
import cz.upce.bvwa2.database.table.Users
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.Clock

object DataFactory {
    fun init() {
        val driverClassName = "org.sqlite.JDBC"
        val jdbcURL = "jdbc:sqlite:file:./build/db"
        val database = Database.connect(jdbcURL, driverClassName)

        transaction(database) {
            SchemaUtils.create(Communications)
            SchemaUtils.create(Users)
            SchemaUtils.create(Roles)
            SchemaUtils.create(Sessions)
            Roles.insert {
                it[roleName] = "ADMIN"
            }
            Roles.insert {
                it[roleName] = "USER"
            }
        }
    }
}