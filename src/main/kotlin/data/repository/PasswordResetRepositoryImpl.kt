package data.repository

import com.example.plugins.DatabaseFactory.dbQuery
import data.model.PasswordResetParams
import domain.repositories.PasswordResetRepository
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.SqlExpressionBuilder.greaterEq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import tables.PasswordResetCode
import java.time.LocalDateTime

class PasswordResetRepositoryImpl : PasswordResetRepository {
    override suspend fun insertCode(passwordResetParams: PasswordResetParams) {
        return dbQuery {
            PasswordResetCode
                .insert { table ->
                    table[code] = passwordResetParams.code
                    table[userId] = passwordResetParams.userId
                    table[expireAt] = passwordResetParams.expireAt
                }
        }
    }

    override suspend fun checkCode(code: Int): PasswordResetParams? {
        return dbQuery {
            PasswordResetCode
                .select {
                    (PasswordResetCode.code eq code) and
                            (PasswordResetCode.expireAt greaterEq LocalDateTime.now())
                }
                .map { it.toPasswordResetParams() }
                .singleOrNull()
        }
    }

    override suspend fun deleteByCode(code: Int) {
        return dbQuery {
            PasswordResetCode
                .deleteWhere() {
                    PasswordResetCode.code eq code
                }
        }
    }

    override suspend fun getByCode(code: Int): PasswordResetParams {
        return dbQuery {
            PasswordResetCode
                .select {
                    (PasswordResetCode.code eq code)
                }
                .map { it.toPasswordResetParams() }
                .single()
        }
    }
}

fun ResultRow.toPasswordResetParams(): PasswordResetParams {
    return PasswordResetParams(
        code = this[PasswordResetCode.code],
        userId = this[PasswordResetCode.userId],
        expireAt = this[PasswordResetCode.expireAt]
    )
}

