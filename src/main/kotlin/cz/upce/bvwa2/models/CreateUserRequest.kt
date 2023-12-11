package cz.upce.bvwa2.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateUserRequest(
    @SerialName(value = "first_name") val firstName: String,
    @SerialName(value = "last_name") val lastName: String,
    @SerialName(value = "email") val email: String,
    @SerialName(value = "phone") val phone: String,
    @SerialName(value = "gender") val gender: String,
    @SerialName(value = "user") val user: String,
    @SerialName(value = "password") val password: String
)