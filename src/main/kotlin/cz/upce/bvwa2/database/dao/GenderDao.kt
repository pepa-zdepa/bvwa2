package cz.upce.bvwa2.database.dao

import cz.upce.bvwa2.database.table.Genders
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.selectAll

// Třída GenderDao poskytuje metody pro přístup k pohlavím uživatelů v databázi.
class GenderDao : IGenderDao {
    // Získání seznamu všech pohlaví.
    override fun getAll(): List<String> {
        return Genders.selectAll().map(::mapRowToEntity)
    }

    // Privátní metoda pro mapování dat z databáze do názvu pohlaví.
    private fun mapRowToEntity(row: ResultRow): String {
        return row[Genders.genderName]
    }
}
