package cz.upce.bvwa2.routes.admin

import io.ktor.resources.*
import io.ktor.server.routing.*

@Resource("/admin")
class Admin {
    @Resource("/user/{id}")
    data class User(val parent: Admin = Admin(), val id: String = "") {
        @Resource("/role")
        data class Role(val parent: User = User())
    }

    @Resource("/users")
    data class Users(val parent: Admin = Admin())
}

fun Route.audminRoutes() {

}