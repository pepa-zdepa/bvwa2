package cz.upce.bvwa2.routes.admin

import cz.upce.bvwa2.auth.UserPrincipal
import cz.upce.bvwa2.repository.UserRepository
import cz.upce.bvwa2.utils.IdConverter
import io.github.omkartenkale.ktor_role_based_auth.withRole
import io.ktor.http.*
import io.ktor.resources.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.resources.*
import io.ktor.server.resources.put
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.kodein.di.instance
import org.kodein.di.ktor.closestDI

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
    val userRepository by closestDI().instance<UserRepository>()
    val idConverter by closestDI().instance<IdConverter>()
    withRole("admin") {
/*        put<Admin.User.Role> {
            val userId = call.principal<UserPrincipal>()!!.userId
            val role = it.newRole

            userRepository.updateRole(userId, role)
            call.respond(HttpStatusCode.OK)
        }*/

        get<Admin.User> {
            val userId = it.id.toLong()
            val user = userRepository.getUserById(userId)
            call.respond(user)
        }

        put<Admin.User> {
            call.respond(HttpStatusCode.OK)
        }

        delete<Admin.User> {
            val userId = it.id
            userRepository.delete(userId.toLong())
            call.respond(HttpStatusCode.OK)
        }

        get<Admin.Users> {
            val users = userRepository.getAllUsers()
            call.respond(users)
        }
    }
}