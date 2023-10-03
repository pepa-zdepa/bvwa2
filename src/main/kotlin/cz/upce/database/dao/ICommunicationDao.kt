package cz.upce.database.dao

import cz.upce.database.model.Communication

interface ICommunicationDao {
    fun getAll(): List<Communication>

    fun getById(id: Long): Communication

    fun getFromTo(idFrom: Long, idTo: Long): List<Communication>

    fun add(communication: Communication)
}