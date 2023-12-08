package cz.upce.bvwa2.database.dao

import cz.upce.bvwa2.database.model.Gender
import cz.upce.bvwa2.database.table.Genders
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll

class GenderDao : IGenderDao {
    override fun getAll(): List<Gender> {
        return Genders.selectAll().map(::mapRowToEntity)
    }

    override fun getByName(name: String): Gender? {
        return Genders.select { Genders.genderName eq name }
            .singleOrNull()
            ?.let(::mapRowToEntity)
    }

    private fun mapRowToEntity(row: ResultRow): Gender {
        return Gender.valueOf(row[Genders.genderName].uppercase())
    }
}
