package data.model

import kotlinx.serialization.Serializable
import java.time.LocalDateTime

data class PasswordResetParams(
    val token: String,
    val userId: Int,
    val expireAt: LocalDateTime
)
