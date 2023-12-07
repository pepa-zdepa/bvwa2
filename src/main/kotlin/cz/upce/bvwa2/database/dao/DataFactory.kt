package cz.upce.bvwa2.database.dao

import cz.upce.bvwa2.database.table.*
import cz.upce.bvwa2.database.table.Roles.roleName
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
/*            SchemaUtils.create(Communications)
            SchemaUtils.create(Users)
            SchemaUtils.create(Roles)
            SchemaUtils.create(Sessions)
            SchemaUtils.create(Sexs)
            Roles.insert {
                it[roleName] = "ADMIN"
            }
            Roles.insert {
                it[roleName] = "USER"
            }
            Sexs.insert {
                it[sexName] = "MALE"
            }
            Sexs.insert {
                it[sexName] = "FEMALE"
            }*/
        }
    }
}