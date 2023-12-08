package cz.upce.bvwa2.database.dao

import cz.upce.bvwa2.database.model.Role

interface IRoleDao {
    fun getAll(): List<Role>

    fun getByName(name: String): Role?

}