package cz.upce.bvwa2.database.dao

import cz.upce.bvwa2.config
import cz.upce.bvwa2.database.Converter
import cz.upce.bvwa2.database.PersistenceException
import cz.upce.bvwa2.database.converter
import cz.upce.bvwa2.database.encryption.Encryption
import cz.upce.bvwa2.database.encryption.EncryptionFactory.encryption
import cz.upce.bvwa2.database.encryption.EncryptionFactory.secretKey
import cz.upce.bvwa2.database.model.Role
import cz.upce.bvwa2.database.model.Sex
import cz.upce.bvwa2.database.model.User
import cz.upce.bvwa2.database.table.Users
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import javax.crypto.SecretKey
import javax.crypto.spec.SecretKeySpec
import java.util.Base64

class UserDao: IUserDao {
    private val encodedKey  = config.encryptKey.key
    private val encryption = Encryption()
    private val key = encryption.getSecretKeyFromEncodedString(encodedKey)

    private val converter = Converter()
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
        println("PEPA")
        println(key)
        try {
            Users.insert {
                it[firstName] = encryption.encrypt(user.firstName, key)
                it[lastName] = encryption.encrypt(user.lastName, key)
                it[password] = converter.hashPassword(user.password)
                it[img] = user.img
                it[role] = user.role
                it[nickName] = user.nickName
                it[email] = encryption.encrypt(user.email, key)
                it[sex] = user.sex
                it[phoneNumber] = encryption.encrypt(user.phoneNumber, key)
            }
        } catch (e: Exception) {
            throw PersistenceException("Chyba při vkládání chyby do databáze", e)
        }
    }

    override fun update(user: User) {
        try {
            Users.update({ Users.id eq user.id }) {
                it[firstName] = encryption.encrypt(user.firstName, key)
                it[lastName] = encryption.encrypt(user.lastName, key)
                it[img] = user.img
                it[role] = user.role
                it[nickName] = user.nickName
                it[email] = encryption.encrypt(user.email, key)
                it[sex] = user.sex
                it[phoneNumber] = encryption.encrypt(user.phoneNumber, key)
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
            encryption.decrypt(row[Users.firstName], key),
            encryption.decrypt(row[Users.lastName], key),
            row[Users.password],
            row[Users.img],
            row[Users.role],
            row[Users.nickName],
            encryption.decrypt(row[Users.email], key),
            row[Users.sex],
            encryption.decrypt(row[Users.phoneNumber], key),
        )
        user.id = row[Users.id]

        return user
    }
}