package cz.upce.bvwa2.database.dao

import cz.upce.bvwa2.database.model.User

interface IUserDao {
    fun getAll(): List<User>

    fun getById(id: Long): User?

    fun getByNickname(nickName: String): User?

    fun add(user: User)

    fun update(user: User)

    fun delete(id: Long)
}