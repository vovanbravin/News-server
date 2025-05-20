package domain.model

import kotlinx.serialization.Serializable

@Serializable
data class PasswordReset(
    val email: String
)
