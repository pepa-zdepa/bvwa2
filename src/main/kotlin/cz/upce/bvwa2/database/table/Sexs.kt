package cz.upce.bvwa2.database.table

import cz.upce.bvwa2.database.table.Roles.autoIncrement
import org.jetbrains.exposed.sql.Table

object Sexs : Table() {
    val id = long("id").autoIncrement()
    val sexName = varchar("sexName", 10)

    override val primaryKey = PrimaryKey(id)
}