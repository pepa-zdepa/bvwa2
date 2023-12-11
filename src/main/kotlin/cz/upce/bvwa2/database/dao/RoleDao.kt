package cz.upce.bvwa2.database.dao

import cz.upce.bvwa2.database.table.Roles
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.selectAll

// Třída RoleDao poskytuje metody pro přístup k rolím uloženým v databázi.
class RoleDao : IRoleDao {
    // Získání seznamu všech rolí.
    override fun getAll(): List<String> {
        return Roles.selectAll().map(::mapRowToEntity)
    }

    // Privátní metoda pro mapování dat z databáze do názvu role.
    private fun mapRowToEntity(row: ResultRow): String {
        return row[Roles.roleName]
    }
}