package cz.upce.bvwa2.database.dao

import cz.upce.bvwa2.database.table.Roles
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.selectAll

class RoleDao : IRoleDao {
    override fun getAll(): List<String> {
        return Roles.selectAll().map(::mapRowToEntity)
    }

    private fun mapRowToEntity(row: ResultRow): String {
        return row[Roles.roleName]
    }
}