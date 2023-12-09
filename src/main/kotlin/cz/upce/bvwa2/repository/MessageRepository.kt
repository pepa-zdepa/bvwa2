package cz.upce.bvwa2.repository

import cz.upce.bvwa2.database.PersistenceException
import cz.upce.bvwa2.database.dao.IMessagesDao
import cz.upce.bvwa2.database.dao.IUserDao
import cz.upce.bvwa2.database.model.Message
import cz.upce.bvwa2.models.MessageRequest
import cz.upce.bvwa2.models.MessageResponse
import org.jetbrains.exposed.sql.transactions.transaction

class MessageRepository(
    private val userDao: IUserDao,
    private val messagesDao: IMessagesDao
) {
    fun add(userId: Long, messageRequest: MessageRequest) = transaction {
        val userFrom = userDao.getById(userId)?.nickName!!

        messagesDao.add(Message.fromRequest(messageRequest, userFrom))
    }

    fun getById(messageId: Long, userId: Long): MessageResponse = transaction {
        val message = messagesDao.getById(messageId)!!
        if (checkUser(message, userId)) {
            Message.toResponse(message)
        } else {
            throw PersistenceException("uživatel s tímto id nemá právo přečíct email")
        }

    }

    fun getAllMessages(userId: Long): List<MessageResponse> = transaction {
        messagesDao.getByUserId(userId).map { Message.toResponse(it) }
    }

    fun updateMessageSeen(messageId: Long, userId: Long) = transaction {
        val message = messagesDao.getById(messageId) !!
        if (checkUser(message, userId)) {
            messagesDao.updateMessageSeen(messageId)
        } else {
            throw PersistenceException("uživatel s tímto id nemá právo aktualizovat email")
        }
    }
    fun checkUser(message: Message, userId: Long): Boolean {
        val user = userDao.getById(userId) ?: throw PersistenceException("uživatel s tímto id neexistuje")
        return user.nickName == message.to || user.nickName == message.from
    }

    fun getUnseenMessages(userId: Long): Int = transaction {
        val userFrom = userDao.getById(userId)?.nickName!!
        val userTo = userDao.getById(userId)?.nickName!!
        messagesDao.numberOfUnseenMessages(userFrom, userTo)
    }


}