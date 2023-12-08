package cz.upce.bvwa2.database.dao

import cz.upce.bvwa2.database.PersistenceException
import cz.upce.bvwa2.database.encryption.Encryption
import cz.upce.bvwa2.database.model.Message
import cz.upce.bvwa2.database.table.Messages
import cz.upce.bvwa2.database.table.Users
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.statements.api.ExposedBlob

class MessagesDao(
    private val encryption: Encryption,
) : IMessagesDao {
    override fun getAll(): List<Message> {
        return Messages.selectAll().map(::mapRowToEntity)
    }

    override fun getById(id: Long): Message? {
        return Messages
            .select { Messages.id eq id }
            .map(::mapRowToEntity)
            .singleOrNull()
    }

    override fun getByUserId(id: Long): List<Message> {
        return Messages.selectAll().map(::mapRowToEntity)
    }

    override fun getFromTo(from: String, to: String): List<Message> {
        return Messages.selectAll()
            .andWhere { Messages.from eq from }
            .andWhere { Messages.to eq to }
            .map(::mapRowToEntity)
    }

    override fun updateMessageSeen(id: Long) {
        try {
            Messages.update({ Messages.id eq id }) {
                it[seen] = true
            }
        } catch (e: Exception) {
            throw PersistenceException("Chyba při upload seen v databázi", e)
        }
    }

    override fun add(messageIn: Message) {
        try {
            Messages.insert {
                it[subject] = encryption.encrypt(messageIn.subject)
                it[from] = messageIn.from
                it[to] = messageIn.to
                it[message] = encryption.encrypt(messageIn.message)
                it[time] = messageIn.time
                it[responseTo] = messageIn.responseTo
                it[time] = messageIn.time
                it[seen] = messageIn.seen
            }
        } catch (e: Exception) {
            throw PersistenceException("Chyba při vkládání chyby do databáze", e)
        }
    }

    private fun mapRowToEntity(row: ResultRow) : Message {
        val message = Message(
            encryption.decrypt(row[Messages.subject]),
            row[Messages.from],
            row[Messages.to],
            encryption.decrypt(row[Messages.message]),
            row[Messages.time],
            row[Messages.responseTo],
            row[Messages.seen]
        )
        message.id = row[Messages.id]

        return message
    }
}