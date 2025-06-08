package domain.repositories

import data.model.User

interface UserRepository {

    suspend fun insertUser(user: User): Exception?

    suspend fun getUserByEmail(email: String): User?

    suspend fun resetPassword(userId: Int, password: String)

}