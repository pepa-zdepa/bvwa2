package cz.upce.bvwa2

import cz.upce.bvwa2.database.dao.sessionDao
import cz.upce.bvwa2.database.model.Session
import io.ktor.server.sessions.*
import org.jetbrains.exposed.sql.transactions.transaction

class SessionRepo {

    fun getById(id: String): Session? = transaction{
        sessionDao.getByid(id)
    }
    fun add(id: String, value: String) = transaction{
        sessionDao.add(id, value)
    }

    fun delete(id: String) = transaction {
        sessionDao.delete(id)
    }
}

val sessionRepo = SessionRepo()