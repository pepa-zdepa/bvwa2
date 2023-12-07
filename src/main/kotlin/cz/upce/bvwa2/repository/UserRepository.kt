package cz.upce.bvwa2.repository

import cz.upce.bvwa2.database.PersistenceException
import cz.upce.bvwa2.database.dao.*
import cz.upce.bvwa2.database.encryption.Encryption
import cz.upce.bvwa2.database.model.Role
import cz.upce.bvwa2.database.model.Sex
import cz.upce.bvwa2.database.model.User
import cz.upce.bvwa2.models.CreateUserRequest
import cz.upce.bvwa2.models.UpdateUserRequest
import cz.upce.bvwa2.models.UserResponse
import org.jetbrains.exposed.sql.transactions.transaction

class UserRepository {

    val userDao: IUserDao = UserDao()
    val roleDao: IRoleDao = RoleDao()
    val sexDao: ISexDao = SexDao()
    fun add(createUserRequest: CreateUserRequest) = transaction{
        val user = userDao.getByNickname(createUserRequest.user)
        val roleId: Short = 2
        val sexId: Short = when(Sex.valueOf(createUserRequest.gender.uppercase())) {
            Sex.MALE -> 1
            else -> 2
        }
        if (user == null) {
                userDao.add(User.fromRequest(createUserRequest, roleId, sexId))
        } else {
            throw PersistenceException("uživatel jiz existuje")
        }
    }

    fun getUser(userId: Long): UserResponse = transaction{
        val user = userDao.getById(userId)
        if (user != null) {
            val role = Role.valueOf(roleDao.getById(user.role.toLong()).toString()).toString()
            val gender = Sex.valueOf(sexDao.getById(user.sex.toLong()).toString()).toString()
            User.toRequest(user, role, gender)
        } else {
            throw PersistenceException("uživatel s tímto id neexistuje")
        }
    }

    fun update(userId: Long, createUserRequest: UpdateUserRequest) = transaction {
        val userById = userDao.getById(userId)
        val userByNickname = createUserRequest.user?.let { userDao.getByNickname(it) }
        if (userById != null && userByNickname != null) {
            val roleId: Short = 2
            val sexId: Short = when(createUserRequest.gender?.let { Sex.valueOf(it.uppercase()) }) {
                Sex.MALE -> 1
                else -> 2
            }
            User.fromRequestUp(createUserRequest, roleId, sexId)?.let { userDao.update(it) }
        } else {
            throw PersistenceException("uživatel s tímto id neexistuje")
        }
    }

    fun delete(userId: Long) = transaction {
        val user = userDao.getById(userId)
        if (user != null) {
            userDao.delete(userId)
        } else {
            throw PersistenceException("uživatel s tímto id neexistuje")
        }
    }

}