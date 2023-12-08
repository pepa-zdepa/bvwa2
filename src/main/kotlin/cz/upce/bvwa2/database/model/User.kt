package cz.upce.bvwa2.database.model

import cz.upce.bvwa2.models.CreateUserRequest
import cz.upce.bvwa2.models.UpdateUserRequest
import cz.upce.bvwa2.models.UserResponse

data class User(
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
    var id: Long = 0

    companion object {
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

        fun fromRequestUp(requestUser: UpdateUserRequest, role: String): User? {
            return User(
                firstName = requestUser.firstName,
                lastName = requestUser.lastName,
                role = role,
                nickName = requestUser.user,
                email = requestUser.email,
                gender =requestUser.gender,
                phoneNumber = requestUser.phone,
                img = ByteArray(0),
                password = "pepa"
            )
        }

        fun toRequest(user: User): UserResponse {
            return UserResponse(
                firstName = user.firstName,
                lastName = user.lastName,
                email = user.email,
                img = user.img ?: ByteArray(0),
                phone = user.phoneNumber,
                role = user.role,
                gender = user.gender,
                user = user.nickName
            )
        }
    }
}