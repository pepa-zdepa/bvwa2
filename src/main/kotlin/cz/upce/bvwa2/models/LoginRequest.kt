package cz.upce.bvwa2.models

import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(val id: Long, val role: String, val nickname: String)