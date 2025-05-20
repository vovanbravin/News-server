package domain.model

import data.model.User
import kotlinx.serialization.Serializable


@Serializable
data class AuthRespond(
    val user: User,
    val token: String
)
