package cz.upce.database.table

import cz.upce.database.model.User
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp

class Communications : Table() {
    val id = long("id").autoIncrement()
    val subject = varchar("subject", 50)
    val from = long("from")
    val to = long("to")
    val message = varchar("message", 255)
    val time = timestamp("time")

    override val primaryKey = PrimaryKey(id)
}