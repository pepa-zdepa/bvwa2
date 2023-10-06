package cz.upce.database.dao

import cz.upce.database.model.User

interface IUserDao {
    fun getAll(): List<User>

    fun getById(id: Long): User?

    fun getByNickname(nickName: String): User?

    fun add(user: User)
}