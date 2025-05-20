package data.repository

import com.example.plugins.DatabaseFactory.dbQuery
import data.model.User
import domain.repositories.UserRepository
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import tables.UserTable

class UserRepositoryImpl: UserRepository {
    override suspend fun insertUser(user: User): Exception? {
        return dbQuery{
            try {
                UserTable
                    .insert { table->
                        table[email] = user.email
                        table[password] = user.password
                        table[nickname] = user.nickname
                    }
                null
            }catch (ex: Exception)
            {
                ex
            }
        }

    }

    override suspend fun getUserByEmail(email: String): User? {
        return dbQuery{
            UserTable
                .select {
                    UserTable.email eq email
                }
                .map { it.toUser() }
                .singleOrNull()
        }
    }
}

fun ResultRow.toUser(): User
{
    return User(
        this[UserTable.id],
        this[UserTable.email],
        this[UserTable.password],
        this[UserTable.nickname]
    )
}
