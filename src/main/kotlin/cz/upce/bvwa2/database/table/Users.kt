package cz.upce.bvwa2.database.table

import org.jetbrains.exposed.sql.Table

object Users : Table() {
    val id = long("id").autoIncrement()
    val firstName = varchar("firstname", 20)
    val lastName = varchar("lastname", 20)
    val password = varchar("password", 20)
    val img = binary("img")
    val role = short("role")
    val nickName = varchar("nickname", 50)

    override val primaryKey = PrimaryKey(id)
}