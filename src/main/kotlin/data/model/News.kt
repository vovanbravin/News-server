package data.model

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
data class News(
    var id: Int?,
    var category: NewsCategory,
    var title: String,
    var description: String,
    var content: String,
    var author: String,
    var images: List<String> = listOf(),
    @Contextual val time: LocalDateTime
)
