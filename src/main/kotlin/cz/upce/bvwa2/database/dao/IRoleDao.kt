package cz.upce.bvwa2.database.dao

import cz.upce.bvwa2.database.model.Role

interface IRoleDao {
    fun getAll(): List<Role>

    fun getById(name: String): Role?

}