package cz.upce.bvwa2.routes.user

import cz.upce.bvwa2.database.PersistenceException
import cz.upce.bvwa2.models.CreateUserRequest
import cz.upce.bvwa2.models.UpdateUserRequest
import cz.upce.bvwa2.repository.UserRepository
import io.ktor.http.*
import io.ktor.resources.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.kodein.di.instance
import org.kodein.di.ktor.closestDI

@Resource("/user")
class User {
    @Resource("/messages")
    class Messages(val parent: User = User()) {
        @Resource("/{messageId}")
        class ById(val parent: Messages = Messages(), val messageId: String)
    }
}

fun Route.userRoutes() {
    val userRepository by closestDI().instance<UserRepository>()

    route("/user") {
        get("/{userId}") {
            val userId = call.parameters["userId"]?.toLongOrNull()
            if (userId == null) {
                call.respond(HttpStatusCode.BadRequest, "Invalid user ID.")
            } else {
                try {
                    val userResponse = userRepository.getUser(userId)
                    call.respond(userResponse)
                } catch (e: PersistenceException) {
                    call.respond(HttpStatusCode.NotFound, e.message ?: "User not found.")
                }
            }
        }
        post {
            val requestUser = call.receive<CreateUserRequest>()
            userRepository.add(requestUser)
            call.respond(HttpStatusCode.OK)
        }
        put("/{userId}") {
            val userId = call.parameters["userId"]?.toLongOrNull()
            val requestUser = call.receive<UpdateUserRequest>()
            if (userId == null) {
                call.respond(HttpStatusCode.BadRequest, "Invalid user ID.")
            } else {
                try {
                    userRepository.update(userId, requestUser)
                    call.respond(HttpStatusCode.OK)
                } catch (e: PersistenceException) {
                    call.respond(HttpStatusCode.NotFound, e.message ?: "User not found.")
                }
            }
        }
        delete("/{userId}") {
            val userId = call.parameters["userId"]?.toLongOrNull()
            if (userId == null) {
                call.respond(HttpStatusCode.BadRequest, "Invalid user ID.")
            } else {
                try {
                    val userResponse = userRepository.delete(userId)
                    call.respond(userResponse)
                } catch (e: PersistenceException) {
                    call.respond(HttpStatusCode.NotFound, e.message ?: "User not found.")
                }
            }
        }
    }

    // Nested routes for /user/messages
    route("/user/messages") {
        get {
            // Code to return all messages for a user
        }
        get("{messageId}") {
            // Code to return a message by messageId
        }
        delete("{messageId}") {
            // Code to delete a message by messageId
        }
    }
}
