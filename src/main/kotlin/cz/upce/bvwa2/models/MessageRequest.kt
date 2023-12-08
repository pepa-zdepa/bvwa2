package cz.upce.bvwa2.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MessageRequest(
    @SerialName(value = "from") val from: String,
    @SerialName(value = "to") val to: String,
    @SerialName(value = "subject") val subject: String,
    @SerialName(value = "message") val message: String,
    @SerialName(value = "responseTo") val responseTo: Long?
)