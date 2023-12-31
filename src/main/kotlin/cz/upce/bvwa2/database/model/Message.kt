package cz.upce.bvwa2.database.model

import cz.upce.bvwa2.models.MessageRequest
import cz.upce.bvwa2.models.MessageResponse
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

// Datová třída Message reprezentuje zprávy mezi uživateli.
data class Message(
    // Atributy zprávy
    val subject: String,
    val from: String,
    val to: String,
    val message: String,
    val time: Instant,
    val responseTo: Long?,
    val seen: Boolean
) {
    // Unikátní ID zprávy
    var id: Long = 0

    companion object {
        // Pomocné metody pro konverzi mezi různými reprezentacemi zprávy.
        fun fromRequest(requestMessage: MessageRequest, nickName: String, responseTo: Long?): Message {
            return Message(
                subject = requestMessage.subject,
                from = nickName,
                to = requestMessage.to,
                message = requestMessage.message,
                time = Clock.System.now(),
                responseTo = responseTo,
                seen = false
            )
        }

        fun toResponse(message: Message, messageId: String, responseTo: String?): MessageResponse {
            return MessageResponse(
                id = messageId,
                subject = message.subject,
                from = message.from,
                to = message.to,
                message = message.message,
                time = message.time,
                responseTo = responseTo,
                seen = message.seen
            )
        }
    }
}