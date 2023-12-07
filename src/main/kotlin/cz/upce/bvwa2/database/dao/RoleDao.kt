package cz.upce.bvwa2.database.dao

import cz.upce.bvwa2.database.converter
import cz.upce.bvwa2.database.model.Role
import cz.upce.bvwa2.database.model.User
import cz.upce.bvwa2.database.table.Roles
import cz.upce.bvwa2.database.table.Users
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll

class RoleDao : IRoleDao {
    override fun getAll(): List<Role> {
        return Roles.selectAll().map(::mapRowToEntity)
    }

    override fun getById(id: Long): Role? {
        return Roles.select { Roles.id eq id }
            .singleOrNull()
            ?.let(::mapRowToEntity)
    }


    private fun mapRowToEntity(row: ResultRow): Role {
        return Role.valueOf(row[Roles.roleName])
    }

}