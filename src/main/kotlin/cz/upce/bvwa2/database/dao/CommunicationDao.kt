package cz.upce.bvwa2.database.dao

import cz.upce.bvwa2.database.PersistenceException
import cz.upce.bvwa2.database.model.Communication
import cz.upce.bvwa2.database.table.Communications
import org.jetbrains.exposed.sql.*

class CommunicationDao : ICommunicationDao {
    override fun getAll(): List<Communication> {
        return Communications.selectAll().map(::mapRowToEntity)
    }

    override fun getById(id: Long): Communication? {
        return Communications
            .select { Communications.id eq id }
            .map(::mapRowToEntity)
            .singleOrNull()
    }

    override fun getFromTo(idFrom: Long, idTo: Long): List<Communication> {
        return Communications.selectAll()
            .andWhere { Communications.from eq idFrom }
            .andWhere { Communications.to eq idTo }
            .map(::mapRowToEntity)
    }

    override fun add(communication: Communication) {
        try {
            Communications.insert {
                it[subject] = communication.subject
                it[from] = communication.from
                it[to] = communication.to
                it[message] = communication.message
                it[time] = communication.time
            }
        } catch (e: Exception) {
            throw PersistenceException("Chyba při vkládání chyby do databáze", e)
        }
    }

    private fun mapRowToEntity(row: ResultRow) : Communication {
        val communication = Communication(
            row[Communications.subject],
            row[Communications.from],
            row[Communications.to],
            row[Communications.message],
            row[Communications.time],
        )
        communication.id = row[Communications.id]

        return communication
    }
}