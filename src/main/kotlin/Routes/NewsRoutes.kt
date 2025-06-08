package Routes

import data.model.News
import data.model.NewsCategory
import data.model.toNewsCategory
import domain.repositories.NewsRepository
import io.ktor.http.HttpStatusCode
import io.ktor.http.content.PartData
import io.ktor.http.content.forEachPart
import io.ktor.http.content.streamProvider
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.principal
import io.ktor.server.request.receive
import io.ktor.server.request.receiveMultipart
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import java.io.File
import java.time.LocalDateTime

fun Route.newsRoutes(newsRepository: NewsRepository) {
    authenticate("jwt-auth") {
        post("news/insert") {
            val multipart = call.receiveMultipart()
            val principal = call.principal<JWTPrincipal>()

            val author = principal?.payload?.getClaim("nickname")?.asString()

            var title: String? = null
            var description: String? = null
            var content: String? = null
            var category: NewsCategory? = null
            var images: ArrayList<String> = arrayListOf()
            val time = LocalDateTime.now()

            multipart.forEachPart { part ->
                when (part) {
                    is PartData.FileItem -> {
                        val filename = part.originalFileName ?: "image_${System.currentTimeMillis()}.jpg"
                        val byteArray = part.streamProvider().readAllBytes()

                        var dir = File("news_images")
                        if (!dir.exists()) {
                            dir.mkdirs()
                        }

                        val file = File(dir, filename)
                        file.writeBytes(byteArray)

                        images.add("news_images/$filename")
                    }

                    is PartData.FormItem -> {
                        when(part.name){
                            "title" -> title = part.value
                            "description" -> description = part.value
                            "content" -> content = part.value
                            "category" -> category = part.value.toNewsCategory()
                        }
                    }

                    else -> Unit
                }
                part.dispose()
            }

            if(title == null){
                call.respond(HttpStatusCode.BadRequest, "Description is empty")
                return@post
            }

            if(description == null){
                call.respond(HttpStatusCode.BadRequest, "Description is empty")
                return@post
            }

            if(content == null){
                call.respond(HttpStatusCode.BadRequest, "Content is empty")
                return@post
            }

            if(category == null){
                call.respond(HttpStatusCode.BadRequest, "Category is empty")
                return@post
            }

            val news = News(
                null,
                category!!,
                title,
                description,
                content,
                author!!,
                images,
                time
            )

            newsRepository.insertNews(news)

            call.respond(HttpStatusCode.OK)
        }
    }

    get("news/get"){
        val page = call.queryParameters["page"]?.toInt() ?: 0
        val news = newsRepository.getDifferentNews(page, 3)

        call.respond(HttpStatusCode.OK, news)
    }

}
