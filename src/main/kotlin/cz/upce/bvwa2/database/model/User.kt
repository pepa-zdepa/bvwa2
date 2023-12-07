package cz.upce.bvwa2.database.model

import cz.upce.bvwa2.models.CreateUserRequest
import cz.upce.bvwa2.models.UpdateUserRequest
import cz.upce.bvwa2.models.UserResponse

data class User(
    val firstName: String,
    val lastName: String,
    val password: String,
    val img: String,
    var role: Short,
    val nickName: String,
    val email: String,
    val sex: Short,
    val phoneNumber: String
) {
    var id: Long = 0

    companion object {
        fun fromRequest(requestUser: CreateUserRequest, roleId: Short, sexId: Short): User {
            return User(
                firstName = requestUser.firstName,
                lastName = requestUser.lastName,
                password = requestUser.password,
                img = requestUser.photo,
                role = roleId,
                nickName = requestUser.user,
                email = requestUser.email,
                sex = sexId,
                phoneNumber = requestUser.phone
            )
        }

        fun fromRequestUp(requestUser: UpdateUserRequest, roleId: Short, sexId: Short): User? {
            return requestUser.firstName?.let {
                requestUser.lastName?.let { it1 ->
                    requestUser.photo?.let { it2 ->
                        requestUser.user?.let { it3 ->
                            requestUser.email?.let { it4 ->
                                requestUser.phone?.let { it5 ->
                                    User(
                                        firstName = it,
                                        lastName = it1,
                                        img = it2,
                                        role = roleId,
                                        nickName = it3,
                                        email = it4,
                                        sex = sexId,
                                        phoneNumber = it5,
                                        password = "pepa"
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        fun toRequest(user: User, role: String, gender: String): UserResponse {
            return UserResponse(
                firstName = user.firstName,
                lastName = user.lastName,
                email = user.email,
                photo = user.img,
                phone = user.phoneNumber,
                role = Role.valueOf(role).toString(),
                gender = Sex.valueOf(gender).toString(),
                user = user.nickName
            )
        }
    }
}