package cz.upce.bvwa2.database.dao

import cz.upce.bvwa2.Config
import cz.upce.bvwa2.database.table.*
import cz.upce.bvwa2.database.table.Roles.roleName
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction

class DataFactory(private val config: Config) {
    fun connect() {
        Database.connect(config.database.connectionString)
    }

    fun init() {
        transaction {
            SchemaUtils.create(Communications)
            SchemaUtils.create(Users)
            SchemaUtils.create(Roles)
            SchemaUtils.create(Sessions)
            SchemaUtils.create(Genders)
            Roles.insert {
                it[roleName] = "ADMIN"
            }
            Roles.insert {
                it[roleName] = "USER"
            }
            Genders.insert {
                it[genderName] = "MALE"
            }
            Genders.insert {
                it[genderName] = "FEMALE"
            }
        }
    }
}