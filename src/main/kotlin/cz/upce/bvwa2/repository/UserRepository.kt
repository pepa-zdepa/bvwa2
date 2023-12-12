package cz.upce.bvwa2.repository

import cz.upce.bvwa2.database.PersistenceException
import cz.upce.bvwa2.database.dao.IGenderDao
import cz.upce.bvwa2.database.dao.IMessagesDao
import cz.upce.bvwa2.database.dao.IRoleDao
import cz.upce.bvwa2.database.dao.IUserDao
import cz.upce.bvwa2.database.model.User
import cz.upce.bvwa2.models.*
import cz.upce.bvwa2.utils.IdConverter
import org.jetbrains.exposed.sql.transactions.transaction

// Třída UserRepository spravuje operace spojené s uživateli v databázi.
class UserRepository(
    // DAO (Data Access Objects) pro různé operace spojené s uživateli, rolemi, pohlavím a zprávami.
    private val userDao: IUserDao,
    private val roleDao: IRoleDao,
    private val genderDao: IGenderDao,
    private val messagesDao: IMessagesDao,
    private val idConverter: IdConverter
) {
    // Přidání nového uživatele do databáze.
    fun add(createUserRequest: CreateUserRequest) = transaction {
        if (!doesUserExist(createUserRequest.user)) {
            userDao.add(User.fromRequest(createUserRequest))
        } else {
            throw PersistenceException("uživatel jiz existuje")
        }
    }

    // Získání informací o uživateli podle jeho ID.
    fun getUser(userId: Long): UserResponse = transaction {
        try {
            val user = userDao.getById(userId)
            User.toResponse(user ?: throw PersistenceException("uživatel s tímto id neexistuje"))
        } catch (e: Exception) {
            throw PersistenceException("uživatel s tímto id neexistuje")
        }
    }

    // Získání uživatele podle jeho přezdívky.
    fun getUserByNickname(nickname: String): User? = transaction {
        userDao.getByNickname(nickname)
    }

    // Aktualizace informací o uživateli.
    fun update(userId: Long, createUserRequest: UpdateUserRequest) = transaction {
        val userById = userDao.getById(userId)
        if (userById != null) {
            val role = userById.role
            User.fromRequestUp(createUserRequest, role, userById.nickName, userById.password)?.let { userDao.update(userId, it) }
        } else {
            throw PersistenceException("uživatel s tímto id neexistuje")
        }
    }

    // Aktualizace informací o uživateli adminem.
    fun updateByAdmin(userId: Long, createUserRequest: UpdateUserRequestbyAdmin) = transaction {
        val userById = userDao.getById(userId)
        if (userById != null) {
            User.fromRequestUpByAdmin(createUserRequest, userById.nickName, userById.password)?.let { userDao.update(userId, it) }
            userDao.updateRole(userId, createUserRequest.role)
        } else {
            throw PersistenceException("uživatel s tímto id neexistuje")
        }
    }

    // Nahrání obrázku uživatele.
    fun uploadImg(id: Long, img: ByteArray) = transaction {
        try {
            userDao.uploadImg(id, img)
        } catch (e: Exception) {
            throw PersistenceException("uživatel s tímto id neexistuje")
        }
    }

    // Aktualizace hesla uživatele.
    fun updatePassword(id: Long, password: String) = transaction {
        try {
            userDao.updatePassword(id, password)
        } catch (e: Exception) {
            throw PersistenceException("uživatel s tímto id neexistuje")
        }
    }

    // Získání obrázku uživatele.
    fun getImg(id: Long): ByteArray = transaction {
        val user = userDao.getById(id)
        user?.img ?: throw PersistenceException("uživatel s tímto id neexistuje")
    }

    // Odstranění uživatele z databáze.
    fun delete(userId: Long) = transaction {
        val user = userDao.getById(userId)
        userDao.delete(userId)
        user?.let { messagesDao.delete(it.nickName) }

    }

    // Aktualizace role uživatele.
    fun updateRole(id: Long, role: String) = transaction{
        userDao.updateRole(id, role)
    }

    // Získání seznamu všech uživatelů.
    fun getAllUsers(): List<UserResponseAdmin> = transaction {
        userDao.getAll().map { User.toResponseAdmin(it, idConverter.encode(it.id)) }
    }

    // Získání uživatele podle ID.
    fun getUserById(id: Long): UserResponse = transaction {
        User.toResponse(userDao.getById(id) ?: throw PersistenceException("uživatel s tímto id neexistuje"))
    }

    // Ověření, zda uživatel existuje.
    fun doesUserExist(userNickName: String): Boolean = transaction {
        userDao.getByNickname(userNickName) != null
    }

    // Získání seznamu všech rolí.
    fun getAllRoles(): List<String> = transaction {
        roleDao.getAll()
    }

    // Získání seznamu všech pohlaví.
    fun getAllGenders(): List<String> = transaction {
        genderDao.getAll()
    }
}