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

    override fun add(messageIn: Message) {
        try {
            Messages.insert {
                it[subject] = messageIn.subject
                it[from] = messageIn.from
                it[to] = messageIn.to
                it[message] = messageIn.message
                it[time] = messageIn.time
                it[responseTo] = messageIn.responseTo
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
            row[Messages.responseTo],
            row[Messages.seen]
        )
        message.id = row[Messages.id]

        return message
    }
}