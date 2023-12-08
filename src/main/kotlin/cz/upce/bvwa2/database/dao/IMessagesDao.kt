package cz.upce.bvwa2.database.dao

import cz.upce.bvwa2.database.model.Message

interface IMessagesDao {
    fun getAll(): List<Message>

    fun getById(id: Long): Message?

    fun getFromTo(idFrom: Long, idTo: Long): List<Message>

    fun add(message: Message)
}