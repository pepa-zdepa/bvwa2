package cz.upce.bvwa2.database.dao

import cz.upce.bvwa2.database.model.Gender

interface IGenderDao {
    fun getAll(): List<Gender>

    fun getByName(name: String): Gender?
}