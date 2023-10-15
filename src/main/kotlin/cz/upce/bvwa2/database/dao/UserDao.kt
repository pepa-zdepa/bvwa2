package cz.upce.bvwa2.database.dao

import cz.upce.bvwa2.database.PersistenceException
import cz.upce.bvwa2.database.converter
import cz.upce.bvwa2.database.model.User
import cz.upce.bvwa2.database.table.Users
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

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
                it[firstName] = user.firstName
                it[lastName] = user.lastName
                it[password] = user.password
                it[img] = user.img
                it[role] = user.role
                it[nickName] = user.nickName
                it[email] = user.email
                it[sex] = user.sex
                it[phoneNumber] = user.phoneNumber
            }
        } catch (e: Exception) {
            throw PersistenceException("Chyba při vkládání chyby do databáze", e)
        }
    }

    override fun update(user: User) {
        try {
            Users.update({ Users.id eq user.id }) {
                it[firstName] = user.firstName
                it[lastName] = user.lastName
                it[password] = user.password
                it[img] = user.img
                it[role] = user.role
                it[nickName] = user.nickName
                it[email] = user.email
                it[sex] = user.sex
                it[phoneNumber] = user.phoneNumber
            }
        } catch (e: Exception) {
            throw PersistenceException("Chyba při update user do databáze", e)
        }
    }

    override fun delete(id: Long) {
        try {
            Users.deleteWhere { (Users.id eq id) }
        } catch (e: Exception) {
            throw PersistenceException("Chyba při delete user v databázi", e)
        }
    }

    private fun mapRowToEntity(row: ResultRow): User {
        val user = User(
            row[Users.firstName],
            row[Users.lastName],
            row[Users.password],
            row[Users.img],
            row[Users.role],
            row[Users.nickName],
            row[Users.email],
            row[Users.sex],
            row[Users.phoneNumber],
        )
        user.id = row[Users.id]

        return user
    }

}

val userDao: IUserDao = UserDao()