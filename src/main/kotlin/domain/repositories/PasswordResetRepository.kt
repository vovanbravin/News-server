package domain.repositories

import data.model.PasswordResetParams

interface PasswordResetRepository {

    suspend fun insertCode(passwordResetParams: PasswordResetParams)

    suspend fun checkCode(code: Int): PasswordResetParams?

    suspend fun deleteByCode(code: Int)

    suspend fun getByCode(code: Int): PasswordResetParams
}