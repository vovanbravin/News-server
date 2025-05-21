package domain.usecase

import authentification.JwtService
import data.model.User
import domain.repositories.UserRepository

class UserUseCase(
    private val jwtService: JwtService,
    private val userRepository: UserRepository
) {

    suspend fun insertUser(user: User): String?
    {
        val ex = userRepository.insertUser(user)

        if(ex == null)
            return null

        else if(ex.message!!.contains("email"))
        {
            return "Email is already exist"
        }
        else if(ex.message!!.contains("nickname"))
        {
            return "Nickname is already exist"
        }
        else
            return ex.message
    }

    suspend fun getUserByEmail(email: String): User?
    {
        return userRepository.getUserByEmail(email)
    }

    fun getJwtVerifier() = jwtService.getVerifier()

    fun generateToken(user: User): String = jwtService.generateToken(user)

    suspend fun resetPassword(userId: Int, hash: String){
        userRepository.resetPassword(userId, hash)
    }

}