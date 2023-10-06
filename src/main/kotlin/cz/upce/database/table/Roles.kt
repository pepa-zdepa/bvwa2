package cz.upce.database.table

import org.jetbrains.exposed.sql.Table

object Roles : Table() {
    val id = integer("id").autoIncrement()
    val roleName = varchar("roleName", 5)

    override val primaryKey = PrimaryKey(id)
}