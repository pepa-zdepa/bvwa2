package cz.upce.bvwa2.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UpdateUserRequest(
    @SerialName(value = "first_name") val firstName: String? = null,
    @SerialName(value = "last_name") val lastName: String? = null,
    @SerialName(value = "email") val email: String? = null,
    @SerialName(value = "phone") val phone: String? = null,
    @SerialName(value = "gender") val gender: String? = null,
    @SerialName(value = "user") val user: String? = null,
    @SerialName(value = "photo") val photo: String? = null
)