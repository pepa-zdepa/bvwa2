package cz.upce.bvwa2.database.model

import java.awt.image.BufferedImage

data class User(
    val firstName: String,
    val lastName: String,
    val password: String,
    val img: BufferedImage,
    var role: Short,
    val nickName: String
) {
    var id: Long = 0
}