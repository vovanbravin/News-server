package domain.usecase

import org.mindrot.jbcrypt.BCrypt

class HashPasswordUseCase {

    fun hashPassword(password: String): String
    {
        return BCrypt.hashpw(password, BCrypt.gensalt())
    }

    fun comparePassword(password: String, hash: String): Boolean
    {
        return BCrypt.checkpw(password, hash)
    }
}