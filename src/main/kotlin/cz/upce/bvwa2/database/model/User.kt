package cz.upce.bvwa2.database.model

data class User(
    val firstName: String,
    val lastName: String,
    val password: String,
    val img: String,
    var role: Short,
    val nickName: String
) {
    var id: Long = 0
}