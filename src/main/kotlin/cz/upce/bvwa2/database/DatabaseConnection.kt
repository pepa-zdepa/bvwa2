package cz.upce.bvwa2.database

import cz.upce.bvwa2.Config
import cz.upce.bvwa2.database.dao.IUserDao
import cz.upce.bvwa2.database.model.User
import cz.upce.bvwa2.database.table.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.insertIgnore
import org.jetbrains.exposed.sql.transactions.transaction

class DatabaseConnection(
    private val config: Config,
    private val userDao: IUserDao
) {
    fun connect() {
        Database.connect(config.database.connectionString)
    }

    fun init() {
        transaction {
            SchemaUtils.create(Messages)
            SchemaUtils.create(Users)
            SchemaUtils.create(Roles)
            SchemaUtils.create(Sessions)
            SchemaUtils.create(Genders)

            Roles.insertIgnore {
                it[roleName] = "admin"
            }
            Roles.insertIgnore {
                it[roleName] = "user"
            }

            Genders.insertIgnore {
                it[genderName] = "Muž"
            }
            Genders.insertIgnore {
                it[genderName] = "Žena"
            }

            runCatching {
                userDao.add(User(
                    "admin",
                    "admin",
                    "admin",
                    null,
                    "admin",
                    "admin",
                    "admin",
                    "Muž",
                    "admin"
                ))
            }
            runCatching {
                userDao.add(User(
                    "user",
                    "user",
                    "user",
                    null,
                    "user",
                    "user",
                    "user",
                    "Muž",
                    "user"
                ))
            }
        }
    }
}