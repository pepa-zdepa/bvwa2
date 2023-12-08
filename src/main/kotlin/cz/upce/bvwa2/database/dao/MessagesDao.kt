package cz.upce.bvwa2.database.dao

import cz.upce.bvwa2.database.PersistenceException
import cz.upce.bvwa2.database.model.Message
import cz.upce.bvwa2.database.table.Messages
import org.jetbrains.exposed.sql.*

class MessagesDao : IMessagesDao {
    override fun getAll(): List<Message> {
        return Messages.selectAll().map(::mapRowToEntity)
    }

    override fun getById(id: Long): Message? {
        return Messages
            .select { Messages.id eq id }
            .map(::mapRowToEntity)
            .singleOrNull()
    }

    override fun getFromTo(idFrom: Long, idTo: Long): List<Message> {
        return Messages.selectAll()
            .andWhere { Messages.from eq idFrom }
            .andWhere { Messages.to eq idTo }
            .map(::mapRowToEntity)
    }

    override fun add(messageI: Message) {
        try {
            Messages.insert {
                it[subject] = messageI.subject
                it[from] = messageI.from
                it[to] = messageI.to
                it[message] = messageI.message
                it[time] = messageI.time
                it[response] = messageI.response
            }
        } catch (e: Exception) {
            throw PersistenceException("Chyba při vkládání chyby do databáze", e)
        }
    }

    private fun mapRowToEntity(row: ResultRow) : Message {
        val message = Message(
            row[Messages.subject],
            row[Messages.from],
            row[Messages.to],
            row[Messages.message],
            row[Messages.time],
            row[Messages.response]
        )
        message.id = row[Messages.id]

        return message
    }
}