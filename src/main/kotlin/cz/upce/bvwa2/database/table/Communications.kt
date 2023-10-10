package cz.upce.bvwa2.database.table

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp

object Communications : Table() {
    val id = long("id").autoIncrement()
    val subject = varchar("subject", 50)
    val from = long("from")
    val to = long("to")
    val message = varchar("message", 255)
    val time = timestamp("time")
    val uuid = uuid("pepa").autoGenerate().index(isUnique = true)

    override val primaryKey = PrimaryKey(id)
}