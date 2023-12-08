package cz.upce.bvwa2.repository

import cz.upce.bvwa2.database.PersistenceException
import cz.upce.bvwa2.database.dao.IGenderDao
import cz.upce.bvwa2.database.dao.IRoleDao
import cz.upce.bvwa2.database.dao.IUserDao
import cz.upce.bvwa2.database.model.User
import cz.upce.bvwa2.models.CreateUserRequest
import cz.upce.bvwa2.models.UpdateUserRequest
import cz.upce.bvwa2.models.UserResponse
import cz.upce.bvwa2.utils.IdConverter
import org.jetbrains.exposed.sql.transactions.transaction

class UserRepository(
    private val userDao: IUserDao,
    private val roleDao: IRoleDao,
    private val genderDao: IGenderDao,
    private val idConverter: IdConverter
) {
    fun add(createUserRequest: CreateUserRequest) = transaction {
        if (!doesUserExist(createUserRequest.user)) {
            userDao.add(User.fromRequest(createUserRequest))
        } else {
            throw PersistenceException("uživatel jiz existuje")
        }
    }

    fun getUser(userId: Long): UserResponse = transaction {
        val user = userDao.getById(userId)
        if (user != null) {
            User.toRequest(user)
        } else {
            throw PersistenceException("uživatel s tímto id neexistuje")
        }
    }

    fun getUserByNickname(nickname: String): User? = transaction {
        userDao.getByNickname(nickname)
    }

    fun update(userId: Long, createUserRequest: UpdateUserRequest) = transaction {
        val userById = userDao.getById(userId)
        val userByNickname = createUserRequest.user.let { userDao.getByNickname(it) }
        if (userById != null && userByNickname != null) {
            val role = userById.role
            User.fromRequestUp(createUserRequest, role)?.let { userDao.update(userId, it) }
        } else {
            throw PersistenceException("uživatel s tímto id neexistuje")
        }
    }

    fun uploadImg(id: Long) = transaction {
        val user = userDao.getById(id)
        if (user != null) {
            //userDao.uploadImg(id)
        } else {
            throw PersistenceException("uživatel s tímto id neexistuje")
        }
    }

    fun updatePassword(id: Long, password: String) = transaction {
        val user = userDao.getById(id)
        if (user != null) {
            userDao.updatePassword(id, password)
        } else {
            throw PersistenceException("uživatel s tímto id neexistuje")
        }
    }

    fun getImg(id: Long): ByteArray = transaction {
        val user = userDao.getById(id)
        user?.img ?: throw PersistenceException("uživatel s tímto id neexistuje")
    }

    fun delete(userId: Long) = transaction {
        val user = userDao.getById(userId)
        if (user != null) {
            userDao.delete(userId)
        } else {
            throw PersistenceException("uživatel s tímto id neexistuje")
        }
    }

    fun doesUserExist(userNickName: String): Boolean{
        return userDao.getByNickname(userNickName) != null
    }

}