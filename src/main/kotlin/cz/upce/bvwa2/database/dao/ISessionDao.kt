package cz.upce.bvwa2.database.dao

import cz.upce.bvwa2.database.model.Session

interface ISessionDao {
    fun getByid(id: String): Session?

    fun add(id: String, value: String)

    fun delete(id: String)
}