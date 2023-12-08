package cz.upce.bvwa2.database.dao

import cz.upce.bvwa2.database.model.Role
import cz.upce.bvwa2.database.table.Roles
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll

class RoleDao : IRoleDao {
    override fun getAll(): List<Role> {
        return Roles.selectAll().map(::mapRowToEntity)
    }

    override fun getByName(name: String): Role? {
        return Roles.select { Roles.roleName eq name }
            .singleOrNull()
            ?.let(::mapRowToEntity)
    }


    private fun mapRowToEntity(row: ResultRow): Role {
        return Role.valueOf(row[Roles.roleName])
    }
}