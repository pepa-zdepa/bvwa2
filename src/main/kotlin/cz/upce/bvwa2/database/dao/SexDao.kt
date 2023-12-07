package cz.upce.bvwa2.database.dao

import cz.upce.bvwa2.database.model.Sex
import cz.upce.bvwa2.database.table.Sexs
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll

class SexDao : ISexDao {
    override fun getAll(): List<Sex> {
        return Sexs.selectAll().map(::mapRowToEntity)
    }

    override fun getById(id: Long): Sex? {
        return Sexs.select { Sexs.id eq id }
            .singleOrNull()
            ?.let(::mapRowToEntity)
    }

    private fun mapRowToEntity(row: ResultRow): Sex {
        return Sex.valueOf(row[Sexs.sexName])
    }
}