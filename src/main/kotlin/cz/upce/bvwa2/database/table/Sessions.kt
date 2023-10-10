package cz.upce.bvwa2.database.table

import org.jetbrains.exposed.sql.Table

object Sessions : Table() {
    val id = varchar("id", 20)
    val data = varchar("data", 250)

    override val primaryKey = PrimaryKey(id)
}