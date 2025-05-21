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
import tables.PasswordResetToken
import java.time.LocalDateTime

class PasswordResetRepositoryImpl : PasswordResetRepository {
    override suspend fun insertToken(passwordResetParams: PasswordResetParams) {
        return dbQuery {
            PasswordResetToken
                .insert { table ->
                    table[token] = passwordResetParams.token
                    table[userId] = passwordResetParams.userId
                    table[expireAt] = passwordResetParams.expireAt
                }
        }
    }

    override suspend fun getByToken(token: String): PasswordResetParams? {
        return dbQuery {
            PasswordResetToken
                .select {
                    (PasswordResetToken.token eq token) and
                            (PasswordResetToken.expireAt greaterEq LocalDateTime.now())
                }
                .map { it.toPasswordResetParams() }
                .singleOrNull()
        }
    }

    override suspend fun deleteByToken(token: String) {
        return dbQuery {
            PasswordResetToken
                .deleteWhere() {
                    PasswordResetToken.token eq token
                }
        }
    }
}

fun ResultRow.toPasswordResetParams(): PasswordResetParams {
    return PasswordResetParams(
        this[PasswordResetToken.token],
        this[PasswordResetToken.userId],
        this[PasswordResetToken.expireAt]
    )
}

