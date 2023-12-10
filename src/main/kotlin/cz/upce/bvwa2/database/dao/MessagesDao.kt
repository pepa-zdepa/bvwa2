package cz.upce.bvwa2.database.dao

import cz.upce.bvwa2.database.PersistenceException
import cz.upce.bvwa2.database.encryption.Encryption
import cz.upce.bvwa2.database.model.Message
import cz.upce.bvwa2.database.table.Messages
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

// Třída MessagesDao poskytuje metody pro přístup a manipulaci se zprávami v databázi.
class MessagesDao(
    // Instance třídy Encryption pro šifrování a dešifrování obsahu zpráv.
    private val encryption: Encryption,
) : IMessagesDao {
    // Získání seznamu všech zpráv.
    override fun getAll(): List<Message> {
        return Messages.selectAll().map(::mapRowToEntity)
    }

    // Získání zprávy podle jejího ID.
    override fun getById(id: Long): Message? {
        return Messages
            .select { Messages.id eq id }
            .map(::mapRowToEntity)
            .singleOrNull()
    }

    // Získání zpráv podle ID uživatele.
    override fun getByUserId(id: Long): List<Message> {
        return Messages.selectAll().map(::mapRowToEntity)
    }

    // Získání zpráv mezi dvěma uživateli.
    override fun getFromTo(from: String, to: String): List<Message> {
        return Messages.selectAll()
            .andWhere { Messages.from eq from }
            .andWhere { Messages.to eq to }
            .map(::mapRowToEntity)
    }

    // Aktualizace stavu zprávy (označení jako přečtené).
    override fun updateMessageSeen(id: Long) {
        try {
            Messages.update({ Messages.id eq id }) {
                it[seen] = true
            }
        } catch (e: Exception) {
            throw PersistenceException("Chyba při upload seen v databázi", e)
        }
    }

    // Přidání nové zprávy do databáze.
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

    // Získání počtu nepřečtených zpráv pro uživatele.
    override fun numberOfUnseenMessages(to: String): Int {
        return Messages
            .select { (Messages.to eq to) and (Messages.seen eq false) }
            .count().toInt()
    }

    // Odstranění zpráv uživatele.
    override fun delete(nickName: String) {
        try {
            Messages.deleteWhere { (to eq nickName) or  (from eq nickName)}
        } catch (e: Exception) {
            throw PersistenceException("Chyba při mazání zpráv v databázi", e)
        }
    }

    // Privátní metoda pro mapování dat z databáze do objektu Message.
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