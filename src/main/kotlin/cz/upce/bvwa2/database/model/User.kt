package cz.upce.bvwa2.database.model

import cz.upce.bvwa2.models.*

// Datová třída User reprezentuje uživatele.
data class User(
    // Atributy uživatele.
    val firstName: String,
    val lastName: String,
    val password: String,
    val img: ByteArray?,
    var role: String,
    val nickName: String,
    val email: String,
    val gender: String,
    val phoneNumber: String
) {
    // Unikátní ID uživatele.
    var id: Long = 0

    companion object {
        // Pomocné metody pro konverzi mezi různými reprezentacemi uživatele.
        fun fromRequest(requestUser: CreateUserRequest): User {
            return User(
                firstName = requestUser.firstName,
                lastName = requestUser.lastName,
                password = requestUser.password,
                img = ByteArray(0),
                role = "user",
                nickName = requestUser.user,
                email = requestUser.email,
                gender = requestUser.gender,
                phoneNumber = requestUser.phone
            )
        }

        fun fromRequestUp(requestUser: UpdateUserRequest, role: String, nickName: String, password: String): User? {
            return User(
                firstName = requestUser.firstName,
                lastName = requestUser.lastName,
                role = role,
                nickName = nickName,
                email = requestUser.email,
                gender =requestUser.gender,
                phoneNumber = requestUser.phone,
                img = null,
                password = password
            )
        }

        fun fromRequestUpByAdmin(requestUser: UpdateUserRequestbyAdmin, nickName: String, password: String): User? {
            return User(
                firstName = requestUser.firstName,
                lastName = requestUser.lastName,
                role = requestUser.role,
                nickName = nickName,
                email = requestUser.email,
                gender =requestUser.gender,
                phoneNumber = requestUser.phone,
                img = null,
                password = password
            )
        }

        fun toResponse(user: User): UserResponse {
            return UserResponse(
                firstName = user.firstName,
                lastName = user.lastName,
                email = user.email,
                phone = user.phoneNumber,
                role = user.role,
                gender = user.gender,
                user = user.nickName
            )
        }

        fun toResponseAdmin(user: User, encodedId: String): UserResponseAdmin {
            return UserResponseAdmin(
                id = encodedId,
                firstName = user.firstName,
                lastName = user.lastName,
                email = user.email,
                phone = user.phoneNumber,
                role = user.role,
                gender = user.gender,
                user = user.nickName
            )
        }
    }
}