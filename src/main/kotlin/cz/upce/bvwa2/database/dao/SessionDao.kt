package cz.upce.bvwa2.database.dao

import cz.upce.bvwa2.database.PersistenceException
import cz.upce.bvwa2.database.model.Session
import cz.upce.bvwa2.database.table.Sessions
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.replace
import org.jetbrains.exposed.sql.select

class SessionDao : ISessionDao {
    override fun getByid(id: String): Session? {
        return Sessions.select { Sessions.id eq id }
            .map(::mapRowToEntity)
            .singleOrNull()
    }

    override fun add(id: String, value: String) {
        try {
            Sessions.replace {
                it[Sessions.id] = id
                it[Sessions.data] = value
            }
        } catch (e: Exception) {
            throw PersistenceException("Chyba při vkládání session do databáze", e)
        }
    }

    override fun delete(id: String) {
        try {
            Sessions.deleteWhere { (Sessions.id eq id) }
        } catch (e: Exception) {
            throw PersistenceException("Chyba při delete sessin v databázi", e)
        }
    }

    private fun mapRowToEntity(row: ResultRow): Session {
        val session = Session(
            row[Sessions.data],
        )
        session.id = row[Sessions.id]

        return session
    }
}

val sessionDao: ISessionDao = SessionDao()