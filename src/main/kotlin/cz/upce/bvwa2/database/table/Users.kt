package cz.upce.bvwa2.database.table

import org.jetbrains.exposed.sql.Table

object Users : Table() {
    val id = long("id").autoIncrement()
    val firstName = varchar("firstname", 100)
    val lastName = varchar("lastname", 100)
    val password = varchar("password", 100)
    val img = varchar("img", 50)
    val role = short("role")
    val nickName = varchar("nickname", 50).index(isUnique = true)
    val email = varchar("email", 100)
    val sex = short("sex")
    val phoneNumber = varchar("phoneNumber", 100)

    override val primaryKey = PrimaryKey(id)
}