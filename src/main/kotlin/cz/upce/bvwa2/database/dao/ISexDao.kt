package cz.upce.bvwa2.database.dao

import cz.upce.bvwa2.database.model.Sex

interface ISexDao {
    fun getAll(): List<Sex>

    fun getById(id: Long): Sex?
}