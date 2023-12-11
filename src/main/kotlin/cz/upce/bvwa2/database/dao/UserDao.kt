package cz.upce.bvwa2.database.dao

import cz.upce.bvwa2.database.PersistenceException
import cz.upce.bvwa2.database.encryption.Encryption
import cz.upce.bvwa2.database.model.User
import cz.upce.bvwa2.database.table.Users
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.statements.api.ExposedBlob

// Třída UserDao poskytuje metody pro přístup a manipulaci s uživatelskými daty v databázi.
class UserDao(
    // Instance třídy Encryption pro šifrování a dešifrování citlivých dat.
    private val encryption: Encryption,
): IUserDao {

    // Získání seznamu všech uživatelů.
    override fun getAll(): List<User> {
        return Users.selectAll().map(::mapRowToEntity)
    }

    // Získání uživatele podle jeho ID.
    override fun getById(id: Long): User? {
        return Users.select { Users.id eq id }
            .map(::mapRowToEntity)
            .singleOrNull()
    }

    // Získání uživatele podle jeho přezdívky.
    override fun getByNickname(nickName: String): User? {
        return Users.select { Users.nickName eq nickName }
            .map(::mapRowToEntity)
            .singleOrNull()
    }

    // Přidání nového uživatele do databáze.
    override fun add(user: User) {
        try {
            Users.insert {
                it[firstName] = encryption.encrypt(user.firstName)
                it[lastName] = encryption.encrypt(user.lastName)
                it[password] = encryption.hashPassword(user.password)
                it[img] = null
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

    // Aktualizace informací o uživateli.
    override fun update(id: Long, user: User) {
        try {
            Users.update({ Users.id eq id }) {
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

    // Odstranění uživatele z databáze.
    override fun delete(id: Long) {
        try {
            Users.deleteWhere { (Users.id eq id) }
        } catch (e: Exception) {
            throw PersistenceException("Chyba při delete user v databázi", e)
        }
    }

    // Nahrání obrázku uživatele.
    override fun uploadImg(id: Long, img: ByteArray) {
        try {
            Users.update({ Users.id eq id }) {
                it[Users.img] = ExposedBlob(img)
            }
        } catch (e: Exception) {
            throw PersistenceException("Chyba při upload img do databáze", e)
        }
    }

    // Aktualizace hesla uživatele.
    override fun updatePassword(id: Long, password: String) {
        try {
            Users.update({ Users.id eq id }) {
                it[Users.password] = encryption.hashPassword(password)
            }
        } catch (e: Exception) {
            throw PersistenceException("Chyba při upload img do databáze", e)
        }
    }

    // Aktualizace role uživatele.
    override fun updateRole(id: Long, role: String) {
        try {
            Users.update({ Users.id eq id }) {
                it[Users.role] = role
            }
        } catch (e: Exception) {
            throw PersistenceException("Chyba při upload img do databáze", e)
        }
    }


    // Privátní metoda pro mapování dat z databáze do objektu User.
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