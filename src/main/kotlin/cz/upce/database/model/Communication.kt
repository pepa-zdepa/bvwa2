package cz.upce.database.model

import kotlinx.datetime.Instant

data class Communication(
    val subject: String,
    val from: Long,
    val to: Long,
    val message: String,
    val time: Instant
) {
    var id: Long = 0
}