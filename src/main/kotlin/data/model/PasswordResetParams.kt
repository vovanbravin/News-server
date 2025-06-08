package data.model

import kotlinx.serialization.Serializable
import java.time.LocalDateTime

data class PasswordResetParams(
    val code: Int,
    val userId: Int,
    val expireAt: LocalDateTime
)
