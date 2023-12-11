package cz.upce.bvwa2.database.table

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp

object Messages : Table() {
    val id = long("id").autoIncrement()
    val subject = varchar("subject", 200)
    val from = varchar("from", 50) references Users.nickName
    val to = varchar("to", 50) references Users.nickName
    val message = varchar("message", 5000)
    val time = timestamp("time")
    val responseTo = long("responseTo").references(id).nullable()
    val seen = bool("seen")

    override val primaryKey = PrimaryKey(id)
}