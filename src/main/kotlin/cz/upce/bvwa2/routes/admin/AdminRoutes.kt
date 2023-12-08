package cz.upce.bvwa2.routes.admin

import io.github.omkartenkale.ktor_role_based_auth.withRole
import io.ktor.http.*
import io.ktor.resources.*
import io.ktor.server.application.*
import io.ktor.server.resources.*
import io.ktor.server.resources.put
import io.ktor.server.response.*
import io.ktor.server.routing.*

@Resource("/admin")
class Admin {
    @Resource("/user/{id}")
    data class User(val parent: Admin = Admin(), val id: String = "") {
        @Resource("/role")
        data class Role(val parent: User = User(), val newRole: String)
    }

    @Resource("/users")
    data class Users(val parent: Admin = Admin())
}

fun Route.adminRoutes() {
    withRole("admin") {
        put<Admin.User.Role> {
            call.respond(HttpStatusCode.OK)
        }

        get<Admin.User> {
            call.respond(HttpStatusCode.OK)
        }

        put<Admin.User> {
            call.respond(HttpStatusCode.OK)
        }

        delete<Admin.User> {
            call.respond(HttpStatusCode.OK)
        }

        get<Admin.Users> {
            call.respond(HttpStatusCode.OK)
        }
    }
}