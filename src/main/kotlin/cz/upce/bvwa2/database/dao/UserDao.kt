package cz.upce.bvwa2.database.dao

import cz.upce.bvwa2.database.PersistenceException
import cz.upce.bvwa2.database.converter
import cz.upce.bvwa2.database.model.User
import cz.upce.bvwa2.database.table.Users
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll

class UserDao : IUserDao {
    override fun getAll(): List<User> {
        return Users.selectAll().map(::mapRowToEntity)
    }

    override fun getById(id: Long): User? {
        return Users.select { Users.id eq id }
            .map(::mapRowToEntity)
            .singleOrNull()
    }

    override fun getByNickname(nickName: String): User? {
        return Users.select { Users.nickName eq nickName }
            .map(::mapRowToEntity)
            .singleOrNull()
    }

    override fun add(user: User) {
        try {
            Users.insert {
                it[Users.firstName] = user.firstName
                it[Users.lastName] = user.lastName
                it[Users.password] = user.password
                it[Users.img] = user.img
                it[Users.role] = user.role
                it[Users.nickName] = user.nickName
            }
        } catch (e: Exception) {
            throw PersistenceException("Chyba při vkládání chyby do databáze", e)
        }
    }

    override fun update(user: User) {
        TODO("Not yet implemented")
    }

    override fun delete(user: User) {
        TODO("Not yet implemented")
    }

    private fun mapRowToEntity(row: ResultRow): User {
        val user = User(
            row[Users.firstName],
            row[Users.lastName],
            row[Users.password],
            row[Users.img],
            row[Users.role],
            row[Users.nickName]
        )
        user.id = row[Users.id]

        return user
    }

}