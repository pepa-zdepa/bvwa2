package cz.upce.bvwa2.routes.user

import cz.upce.bvwa2.auth.UserPrincipal
import cz.upce.bvwa2.models.CreateUserRequest
import cz.upce.bvwa2.models.MessageRequest
import cz.upce.bvwa2.models.UpdateUserRequest
import cz.upce.bvwa2.repository.MessageRepository
import cz.upce.bvwa2.repository.UserRepository
import cz.upce.bvwa2.utils.IdConverter
import cz.upce.bvwa2.utils.ImageConverter
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.resources.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.plugins.cachingheaders.*
import io.ktor.server.request.*
import io.ktor.server.resources.*
import io.ktor.server.resources.post
import io.ktor.server.resources.put
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.utils.io.jvm.javaio.*
import org.kodein.di.instance
import org.kodein.di.ktor.closestDI

@Resource("/user")
class User {
    @Resource("/messages")
    class Messages(val parent: User = User(), val direction: String = "in") {
        @Resource("/{messageId}")
        class ById(val parent: Messages = Messages(), val messageId: String = "") {
            @Resource("/seen")
            class Seen(val parent: ById = ById())
        }
    }

    @Resource("/update-password")
    class UpdatePassword(val parent: User = User())

    @Resource("/upload-image")
    class UploadImage(val parent: User = User())

    @Resource("/{id}/image")
    class Image(val parent: User = User(), val id: String)

    @Resource("/genders")
    class Genders(val parent: User = User())

    @Resource("/roles")
    class Roles(val parent: User = User())
}

fun Route.userRoutes() {
    val userRepository by closestDI().instance<UserRepository>()
    val idConverter by closestDI().instance<IdConverter>()
    val messageRepository by closestDI().instance<MessageRepository>()

    post<User> {
        val requestUser = call.receive<CreateUserRequest>()
        userRepository.add(requestUser)
        call.respond(HttpStatusCode.OK)
    }

    get<User.Genders> {
        call.respond(userRepository.getAllGenders())
    }

    authenticate("session") {
        get<User> {
            val userId = call.principal<UserPrincipal>()!!.userId

            val userResponse = userRepository.getUser(userId)
            call.respond(userResponse)
        }

        put<User> {
            val userId = call.principal<UserPrincipal>()!!.userId
            val requestUser = call.receive<UpdateUserRequest>()

            userRepository.update(userId, requestUser)
            call.respond(HttpStatusCode.OK)
        }

        put<User.UpdatePassword> {
            val userId = call.principal<UserPrincipal>()!!.userId
            val password = call.receive<String>()

            userRepository.updatePassword(userId, password)
            call.respond(HttpStatusCode.OK)
        }

        get<User.Messages> {
            val userId = call.principal<UserPrincipal>()!!.userId
            val messages = messageRepository.getAllMessages(userId)

            call.respond(messages)
        }

        post<User.Messages> {
            val userId = call.principal<UserPrincipal>()!!.userId
            val message = call.receive<MessageRequest>()

            messageRepository.add(userId, message)
            call.respond(it.direction)
        }

        get<User.Messages.ById> {
            val userId = call.principal<UserPrincipal>()!!.userId
            val messageId = it.messageId

            val message = messageRepository.getById(messageId.toLong(), userId)
            call.respond(message)
        }

        post<User.Messages.ById.Seen> {
            val userId = call.principal<UserPrincipal>()!!.userId
            messageRepository.updateMessageSeen(it.parent.messageId.toLong(), userId)

            call.respond(it.parent.messageId)
        }

        get<User.Image> {
            val userId = idConverter.decode(it.id)

            val img = userRepository.getImg(userId)

//        val image = File("test.tiff")
//        val img = ImageConverter.convertImage(image.inputStream())

            call.caching = CachingOptions(CacheControl.MaxAge(maxAgeSeconds = 600))
            call.response.header(HttpHeaders.ContentType, "image/jpeg")
            call.respondBytes(img)
        }

        post<User.UploadImage> {
            val userId = call.principal<UserPrincipal>()!!.userId
            val stream = call.receiveChannel().toInputStream()

//            Validator.validateImage(stream)
            val image = ImageConverter.convertImage(stream)
            userRepository.uploadImg(userId, image)

            call.respond(HttpStatusCode.OK)
        }

        get<User.Roles> {
            call.respond(userRepository.getAllRoles())
        }
    }
}
