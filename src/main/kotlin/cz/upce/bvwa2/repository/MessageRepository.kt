package cz.upce.bvwa2.repository

import cz.upce.bvwa2.database.PersistenceException
import cz.upce.bvwa2.database.dao.IMessagesDao
import cz.upce.bvwa2.database.dao.IUserDao
import cz.upce.bvwa2.database.model.Message
import cz.upce.bvwa2.models.MessageRequest
import cz.upce.bvwa2.models.MessageResponse
import cz.upce.bvwa2.utils.IdConverter
import org.jetbrains.exposed.sql.transactions.transaction

// Třída MessageRepository spravuje operace spojené se zprávami v databázi.
class MessageRepository(
    // DAO pro operace s uživateli a zprávami.
    private val userDao: IUserDao,
    private val messagesDao: IMessagesDao,
    private val idConverter: IdConverter
) {
    // Přidání nové zprávy do databáze.
    fun add(userId: Long, messageRequest: MessageRequest) = transaction {
        val userFrom = userDao.getById(userId)?.nickName!!
        val responseTo = messageRequest.responseTo?.let { idConverter.decode(it) }

        if (userDao.getAll().none { it.nickName == messageRequest.to })
            throw PersistenceException("Uživatel ${messageRequest.to} neexistuje")

        messagesDao.add(Message.fromRequest(messageRequest, userFrom, responseTo))
    }

    // Získání zprávy podle jejího ID a ověření, zda má uživatel právo ji přečíst.
    fun getById(messageId: Long, userId: Long): MessageResponse = transaction {
        val message = messagesDao.getById(messageId)!!
        val responseTo = message.responseTo?.let { idConverter.encode(it) }

        if (checkUser(message, userId)) {
            Message.toResponse(message, idConverter.encode(message.id), responseTo)
        } else {
            throw PersistenceException("uživatel s tímto id nemá právo přečíct email")
        }

    }

    // Získání všech zpráv pro uživatele.
    fun getAllMessages(userId: Long, direction: String): List<MessageResponse>  = transaction {
        val user = userDao.getById(userId) ?: throw PersistenceException("uživatel s tímto id neexistuje")

        messagesDao.getByUserId(userId)
            .map { Message.toResponse(it, idConverter.encode(it.id), idConverter.encode(it.responseTo)) }
            .filter {
                when {
                    direction == "in" && it.to == user.nickName ->  true
                    direction == "out" && it.from == user.nickName -> true
                    else -> false
                }
            }
    }

    // Aktualizace stavu zprávy (označení jako přečtené) pro uživatele.
    fun updateMessageSeen(messageId: Long, userId: Long) = transaction {
        val message = messagesDao.getById(messageId) !!
        if (checkUser(message, userId)) {
            messagesDao.updateMessageSeen(messageId)
        } else {
            throw PersistenceException("uživatel s tímto id nemá právo aktualizovat email")
        }
    }

    // Ověření, zda uživatel má právo pracovat se zprávou.
    fun checkUser(message: Message, userId: Long): Boolean {
        val user = userDao.getById(userId) ?: throw PersistenceException("uživatel s tímto id neexistuje")
        return user.nickName == message.to || user.nickName == message.from
    }

    // Získání počtu nepřečtených zpráv pro uživatele.
    fun getUnseenMessages(userId: Long): Int = transaction {
        val userTo = userDao.getById(userId)?.nickName!!
        messagesDao.numberOfUnseenMessages(userTo)
    }
}