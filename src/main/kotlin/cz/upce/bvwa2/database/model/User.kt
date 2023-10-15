package cz.upce.bvwa2.database.model

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
}