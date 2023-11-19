package cz.upce.bvwa2.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Message(
    @SerialName(value = "id") val id: String,
    @SerialName(value = "from") val from: String,
    @SerialName(value = "to") val to: String,
    @SerialName(value = "subject") val subject: String,
    @SerialName(value = "message") val message: String
)