package cz.upce.bvwa2.database.dao

interface ISessionDao {
    fun write(id: String, value: String)

    fun invalidate(id: String)

    fun read(id: String): String
}