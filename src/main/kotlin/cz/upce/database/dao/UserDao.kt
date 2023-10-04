package cz.upce.database.dao

import cz.upce.database.model.Communication
import cz.upce.database.model.User
import cz.upce.database.table.Communications
import cz.upce.database.table.Users
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.selectAll

class UserDao : IUserDao {
    override fun getAll(): List<User> {
        return Users.selectAll().map(::mapRowToEntity)
    }

    override fun getById(id: Long): User {
        TODO("Not yet implemented")
    }

    override fun getByNickname(nickName: String): User {
        TODO("Not yet implemented")
    }

    override fun add(user: User) {
        TODO("Not yet implemented")
    }

    private fun mapRowToEntity(row: ResultRow) : User {
        val user = User(
            row[Users.firstName],
            row[Users.lastName],
            row[Users.password],
            row[Users.img],
            row[Users.nickName]
        )
        user.id = row[Users.id]
        user.role = row[Users.role]


        return user
    }
}