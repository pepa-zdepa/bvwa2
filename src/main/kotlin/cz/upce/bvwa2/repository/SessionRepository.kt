package cz.upce.bvwa2.repository

import cz.upce.bvwa2.database.dao.ISessionDao
import cz.upce.bvwa2.database.model.Session
import org.jetbrains.exposed.sql.transactions.transaction

class SessionRepository(private val sessionDao: ISessionDao) {

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