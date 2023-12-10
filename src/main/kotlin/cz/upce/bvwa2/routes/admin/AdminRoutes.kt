package cz.upce.bvwa2.routes.admin

import cz.upce.bvwa2.auth.UserPrincipal
import cz.upce.bvwa2.models.UpdateUserRequestbyAdmin
import cz.upce.bvwa2.repository.UserRepository
import cz.upce.bvwa2.utils.IdConverter
import io.ktor.http.*
import io.ktor.resources.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
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
        data class Role(val parent: User = User())

        @Resource("/update-password")
        data class Password(val parent: User = User())
    }

    @Resource("/users")
    data class Users(val parent: Admin = Admin())
}

fun Route.adminRoutes() {
    val userRepository by closestDI().instance<UserRepository>()
    val idConverter by closestDI().instance<IdConverter>()

    put<Admin.User.Role> {
        val userId = idConverter.decode(it.parent.id)
        val role = call.receive<String>()

        userRepository.updateRole(userId, role)

        call.respond(HttpStatusCode.OK)
    }

    get<Admin.User> {
        val userId = idConverter.decode(it.id)

        val user = userRepository.getUserById(userId)

        call.respond(user)
    }

    put<Admin.User> {
        val userId = idConverter.decode(it.id)
        val user = call.receive<UpdateUserRequestbyAdmin>()

        userRepository.updateByAdmin(userId, user)

        call.respond(HttpStatusCode.OK)
    }

    put<Admin.User.Password> {
        val userId = idConverter.decode(it.parent.id)
        val password = call.receive<String>()

        userRepository.updatePassword(userId, password)

        call.respond(HttpStatusCode.OK)
    }

    delete<Admin.User> {
        val userId = idConverter.decode(it.id)
        userRepository.delete(userId)

        call.respond(HttpStatusCode.OK)
    }

    get<Admin.Users> {
        val users = userRepository.getAllUsers()

        call.respond(users)
    }
}