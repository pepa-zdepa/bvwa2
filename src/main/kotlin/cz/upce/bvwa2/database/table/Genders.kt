package cz.upce.bvwa2.database.table

import org.jetbrains.exposed.sql.Table

object Genders : Table() {
    val genderName = varchar("genderName", 10)

    override val primaryKey = PrimaryKey(genderName)
}