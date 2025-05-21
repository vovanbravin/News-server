package domain.repositories

import data.model.PasswordResetParams

interface PasswordResetRepository {

    suspend fun insertToken(passwordResetParams: PasswordResetParams)

    suspend fun getByToken(token: String): PasswordResetParams?

    suspend fun deleteByToken(token: String)

}