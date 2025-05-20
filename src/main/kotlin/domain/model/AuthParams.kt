package domain.model

import kotlinx.serialization.Serializable

@Serializable
data class AuthParams(
    val email: String,
    val password: String
)
