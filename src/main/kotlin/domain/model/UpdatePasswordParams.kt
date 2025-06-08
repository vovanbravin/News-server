package domain.model

import kotlinx.serialization.Serializable

@Serializable
data class UpdatePasswordParams(
    val reset: PasswordReset,
    val newPassword: String
)
