package cz.upce.database.dao

import cz.upce.database.model.Role

interface IRoleDao {
    fun getAll(): List<Role>

    fun getById(id: Long): Role?

}