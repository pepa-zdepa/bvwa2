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
        val userFrom = userDao.getById(userId)?.nickName
            ?: throw PersistenceException("uživatel s tímto id neexistuje")

        messagesDao.add(Message.fromRequest(messageRequest, userFrom))
    }

    fun getById(messageId: Long): MessageResponse = transaction {
        messagesDao.getById(messageId)?.let { Message.toResponse(it) }
            ?: throw PersistenceException("zpráva s tímto id neexistuje")
    }

    fun getAllMessages(userId: Long): List<MessageResponse> = transaction {
        messagesDao.getByUserId(userId).map { Message.toResponse(it) }
    }

    fun updateMessageSeen(messageId: Long) = transaction {
        messagesDao.updateMessageSeen(messageId)
    }

}