package cz.upce.bvwa2.database.dao

import cz.upce.bvwa2.database.model.Message

interface IMessagesDao {
    fun getAll(): List<Message>

    fun getById(id: Long): Message?

    fun getByUserId(id: Long): List<Message>

    fun getFromTo(from: String, to: String): List<Message>

    fun updateMessageSeen(id: Long)

    fun add(message: Message)

    fun numberOfUnseenMessages(to: String): Int
}