package Routes

import data.model.PasswordResetParams
import data.model.User
import data.repository.PasswordResetRepositoryImpl
import domain.model.AuthParams
import domain.model.AuthRespond
import domain.model.PasswordReset
import domain.repositories.PasswordResetRepository
import domain.repositories.UserRepository
import domain.usecase.HashPasswordUseCase
import domain.usecase.ResetPasswordUseCase
import domain.usecase.UserUseCase
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.request.receiveParameters
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import tables.PasswordResetToken
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.UUID
import org.jetbrains.exposed.sql.SqlExpressionBuilder.greaterEq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.update
import tables.UserTable

fun Route.userRoutes(
    userUseCase: UserUseCase,
    hashPasswordUseCase: HashPasswordUseCase,
    resetPasswordUseCase: ResetPasswordUseCase,
    passwordResetRepository: PasswordResetRepository
) {
    post("auth/sign-up") {
        val user = call.receive<User>()

        user.password = hashPasswordUseCase.hashPassword(user.password)

        val message = userUseCase.insertUser(user)

        if (message == null) {
            val userTable = userUseCase.getUserByEmail(user.email)
            val token = userUseCase.generateToken(user)
            call.respond(HttpStatusCode.Created, AuthRespond(userTable!!, token))
            return@post
        }

        call.respond(HttpStatusCode.BadRequest, message)

    }

    post("auth/sign-in") {
        val params = call.receive<AuthParams>()

        val user = userUseCase.getUserByEmail(params.email)

        if (user == null) {
            call.respond(HttpStatusCode.BadRequest, "User not found")
            return@post
        }

        if (!hashPasswordUseCase.comparePassword(params.password, user.password)) {
            call.respond(HttpStatusCode.BadRequest, "Wrong password")
        }

        val token = userUseCase.generateToken(user)
        call.respond(HttpStatusCode.OK, AuthRespond(user, token))
    }

    post("auth/forgot-password") {
        val params = call.receive<PasswordReset>()

        val user = userUseCase.getUserByEmail(email = params.email)

        if (user == null) {
            call.respond(HttpStatusCode.BadRequest, "User not found")
            return@post
        }

        val token = UUID.randomUUID().toString()

        val expireAt = LocalDateTime.now().plusHours(1)

        val passwordResetParams = PasswordResetParams(
            token,
            user.id,
            expireAt
        )

        passwordResetRepository.insertToken(passwordResetParams)


        resetPasswordUseCase.reset(params.email, token)

        call.respond(HttpStatusCode.OK)
    }

    get("/auth/reset-password") {
        val token = call.request.queryParameters["token"] ?: ""
        call.respondText(
            """
        <html>
        <body>
            <form action="/auth/reset-password?token=$token" method="post">
                <input type="hidden" name="token" value="$token"/>
                <label>Новый пароль:</label>
                <input type="password" name="password"/>
                <button type="submit">Сбросить пароль</button>
            </form>
        </body>
        </html>
    """.trimIndent(), ContentType.Text.Html
        )
    }

    post("auth/reset-password") {
        val params = call.receiveParameters()
        val token = params["token"] ?: return@post call.respond(HttpStatusCode.BadRequest)
        val newPassword = params["password"] ?: return@post call.respond(HttpStatusCode.BadRequest)

        val passwordResetParams = passwordResetRepository.getByToken(token)

        if(passwordResetParams ==  null)
        {
            return@post call.respond(HttpStatusCode.BadRequest, "Недействительный или просроченный токен")
        }

        passwordResetRepository.deleteByToken(passwordResetParams.token)

        val hashed = hashPasswordUseCase.hashPassword(newPassword)

        userUseCase.resetPassword(passwordResetParams.userId, hashed)

        call.respond(HttpStatusCode.OK, "Пароль успешно обновлён")
    }
}