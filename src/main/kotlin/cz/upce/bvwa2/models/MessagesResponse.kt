package cz.upce.bvwa2.models

import kotlinx.serialization.Serializable

@Serializable
data class MessagesResponse(
    val messages: List<Message>
)