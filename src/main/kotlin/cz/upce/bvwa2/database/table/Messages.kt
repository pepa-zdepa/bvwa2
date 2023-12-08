package cz.upce.bvwa2.database.table

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp

object Messages : Table() {
    val id = long("id").autoIncrement()
    val subject = varchar("subject", 200)
    val from = long("from") references Users.id
    val to = long("to") references Users.id
    val message = varchar("message", 5000)
    val time = timestamp("time")
    val responseTo = long("responseTo").nullable()
    val seen = bool("seen")

    override val primaryKey = PrimaryKey(id)
}