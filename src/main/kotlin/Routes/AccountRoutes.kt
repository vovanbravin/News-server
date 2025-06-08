package Routes

import data.model.UserAvatar
import domain.repositories.UserAvatarsRepository
import io.ktor.http.HttpStatusCode
import io.ktor.http.content.PartData
import io.ktor.http.content.forEachPart
import io.ktor.http.content.streamProvider
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.principal
import io.ktor.server.http.content.file
import io.ktor.server.request.receiveMultipart
import io.ktor.server.response.respond
import io.ktor.server.response.respondFile
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import tables.UserAvatars
import java.io.File

fun Route.accountRoutes(userAvatarsRepository: UserAvatarsRepository)
{
    get("account/avatar/{userId}"){
        val userId = call.request.pathVariables["userId"]?.toInt()

        if(userId == null){
            call.respond(HttpStatusCode.BadRequest)
            return@get
        }

        val image = userAvatarsRepository.getPhotoById(userId)

        if(image == null){
            call.respond(HttpStatusCode.BadRequest, "Photo not added")
        }

        val file = File("avatars/${image}")

        if(file.exists())
        {
            call.respondFile(file)
        }

        call.respond(HttpStatusCode.BadRequest, "File is not found")
    }

    authenticate("jwt-auth"){
        post("account/avatar/"){
            val principal = call.principal<JWTPrincipal>()
            val userId = principal?.payload?.getClaim("userId")?.asInt()

            if(userId == null){
                call.respond(HttpStatusCode.BadRequest, "Missing userId")
                return@post
            }

            val multiPart = call.receiveMultipart()

            var image: ByteArray? = null
            var filename: String? = null

            multiPart.forEachPart {part->
                when(part){
                    is PartData.BinaryChannelItem -> {

                    }
                    is PartData.BinaryItem -> {

                    }
                    is PartData.FileItem -> {
                        if(part.name == "image") {
                            image = part.streamProvider().readBytes()
                            filename = part.originalFileName
                        }
                    }
                    is PartData.FormItem -> {

                    }
                }
                part.dispose()
            }

            if(image == null) {
                call.respond(HttpStatusCode.BadRequest, "Image not found")
                return@post
            }

            val dir = File("avatars")
            if(!dir.exists()){
                dir.mkdirs()
            }

            val file = File(dir, filename!!)

            file.writeBytes(image)

            val userAvatar = UserAvatar(
                userId = userId,
                filename!!
            )

            userAvatarsRepository.uploadPhoto(userAvatar)

            call.respond(HttpStatusCode.OK)
        }
    }

}