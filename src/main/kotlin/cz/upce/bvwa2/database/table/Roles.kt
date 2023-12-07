package cz.upce.bvwa2.database.table

import org.jetbrains.exposed.sql.Table

object Roles : Table() {
    val id = long("id").autoIncrement()
    val roleName = varchar("roleName", 5)

    override val primaryKey = PrimaryKey(id)
}