package authentification

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import data.model.User
import java.time.LocalDateTime
import java.time.ZoneOffset

class JwtService {

    private val issuer = "bob"
    private val secret = "secret"
    private val algorithm = Algorithm.HMAC256(secret)

    private val jwtVerifier = JWT
        .require(algorithm)
        .withIssuer(issuer)
        .build()

    fun generateToken(user: User): String = JWT
        .create()
        .withSubject("news")
        .withIssuer(issuer)
        .withClaim("email", user.email)
        .withExpiresAt(LocalDateTime.now().plusDays(1).toInstant(ZoneOffset.UTC))
        .sign(algorithm)

    fun getVerifier() = jwtVerifier
}