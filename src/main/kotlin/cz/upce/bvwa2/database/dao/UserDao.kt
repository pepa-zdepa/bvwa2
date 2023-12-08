package cz.upce.bvwa2.database.dao

import cz.upce.bvwa2.database.PersistenceException
import cz.upce.bvwa2.database.encryption.Encryption
import cz.upce.bvwa2.database.model.User
import cz.upce.bvwa2.database.table.Users
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.statements.api.ExposedBlob

class UserDao(
    private val encryption: Encryption,
): IUserDao {
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
                it[firstName] = encryption.encrypt(user.firstName)
                it[lastName] = encryption.encrypt(user.lastName)
                it[password] = encryption.hashPassword(user.password)
                it[img] = ExposedBlob(user.img ?: ByteArray(0))
                it[role] = user.role
                it[nickName] = user.nickName
                it[email] = encryption.encrypt(user.email)
                it[gender] = user.gender
                it[phoneNumber] = encryption.encrypt(user.phoneNumber)
            }
        } catch (e: Exception) {
            throw PersistenceException("Chyba při vkládání uživatele do databáze", e)
        }
    }

    override fun update(user: User) {
        try {
            Users.update({ Users.id eq user.id }) {
                it[firstName] = encryption.encrypt(user.firstName)
                it[lastName] = encryption.encrypt(user.lastName)
                it[email] = encryption.encrypt(user.email)
                it[gender] = user.gender
                it[phoneNumber] = encryption.encrypt(user.phoneNumber)
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
            encryption.decrypt(row[Users.firstName]),
            encryption.decrypt(row[Users.lastName]),
            row[Users.password],
            row[Users.img]?.bytes,
            row[Users.role],
            row[Users.nickName],
            encryption.decrypt(row[Users.email]),
            row[Users.gender],
            encryption.decrypt(row[Users.phoneNumber]),
        )
        user.id = row[Users.id]

        return user
    }
}