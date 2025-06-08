package data.model

import kotlinx.serialization.Serializable

@Serializable
data class UserAvatar(
    val userId: Int,
    val image: String
)