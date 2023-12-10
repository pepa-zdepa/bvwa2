package cz.upce.bvwa2.database.model

import cz.upce.bvwa2.models.MessageRequest
import cz.upce.bvwa2.models.MessageResponse
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

data class Message(
    val subject: String,
    val from: String,
    val to: String,
    val message: String,
    val time: Instant,
    val responseTo: Long?,
    val seen: Boolean
) {
    var id: Long = 0

    companion object {

        fun fromRequest(requestMessage: MessageRequest, nickName: String): Message {
            return Message(
                subject = requestMessage.subject,
                from = nickName,
                to = requestMessage.to,
                message = requestMessage.message,
                time = Clock.System.now(),
                responseTo = requestMessage.responseTo?.toLongOrNull(), // TODO
                seen = false
            )
        }

        fun toResponse(message: Message, messageId: String): MessageResponse {
            return MessageResponse(
                id = messageId,
                subject = message.subject,
                from = message.from,
                to = message.to,
                message = message.message,
                time = message.time,
                responseTo = message.responseTo.toString(), // TODO
                seen = message.seen
            )
        }
    }
}