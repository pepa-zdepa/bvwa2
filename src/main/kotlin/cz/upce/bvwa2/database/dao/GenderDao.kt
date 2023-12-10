package cz.upce.bvwa2.database.dao

import cz.upce.bvwa2.database.table.Genders
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.selectAll

class GenderDao : IGenderDao {
    override fun getAll(): List<String> {
        return Genders.selectAll().map(::mapRowToEntity)
    }

    private fun mapRowToEntity(row: ResultRow): String {
        return row[Genders.genderName]
    }
}
