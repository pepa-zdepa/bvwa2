package cz.upce.bvwa2.database.model

import kotlinx.datetime.Instant
import java.util.UUID

data class Communication(
    val subject: String,
    val from: Long,
    val to: Long,
    val message: String,
    val time: Instant,
) {
    var id: Long = 0
    var uuid: UUID = UUID.randomUUID()
}