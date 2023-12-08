package cz.upce.bvwa2.database.dao

import cz.upce.bvwa2.database.model.User

interface IUserDao {
    fun getAll(): List<User>

    fun getById(id: Long): User?

    fun getByNickname(nickName: String): User?

    fun add(user: User)

    fun update(id: Long, user: User)

    fun delete(id: Long)

    fun uploadImg(id: Long, img: ByteArray)

    fun updatePassword(id: Long, password: String)

    fun updateRole(id: Long, role: String)
}