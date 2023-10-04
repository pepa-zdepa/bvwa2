package cz.upce.database.model

import java.awt.image.BufferedImage

data class User(
    val firstName: String,
    val lastName: String,
    val password: String,
    val img: BufferedImage,
    var role: Roles,
    val nickName: String
) {
    var id: Long = 0
}