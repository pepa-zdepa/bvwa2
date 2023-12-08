package cz.upce.bvwa2.database.model

import kotlinx.datetime.Instant

data class Message(
    val subject: String,
    val from: Long,
    val to: Long,
    val message: String,
    val time: Instant,
    val responseTo: Long?,
    val seem: Boolean
) {
    var id: Long = 0
}