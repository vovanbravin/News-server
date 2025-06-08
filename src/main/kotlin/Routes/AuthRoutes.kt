package Routes

import data.model.PasswordResetParams
import data.model.User
import domain.model.AuthParams
import domain.model.AuthRespond
import domain.model.PasswordReset
import domain.model.UpdatePasswordParams
import domain.repositories.PasswordResetRepository
import domain.usecase.HashPasswordUseCase
import domain.usecase.ResetPasswordUseCase
import domain.usecase.UserUseCase
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDateTime

fun Route.authRoutes(
    userUseCase: UserUseCase,
    hashPasswordUseCase: HashPasswordUseCase,
    resetPasswordUseCase: ResetPasswordUseCase,
    passwordResetRepository: PasswordResetRepository
) {

    post("sign-up") {
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

    post("sign-in") {
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

    post("forgot-password") {
        val params = call.receive<PasswordReset>()

        val user = userUseCase.getUserByEmail(email = params.email)

        if (user == null) {
            call.respond(HttpStatusCode.BadRequest, "User not found")
            return@post
        }

        val verificationCode = (100000..999999).random()

        val expireAt = LocalDateTime.now().plusMinutes(10)

        val passwordResetParams = PasswordResetParams(
            verificationCode,
            user.id,
            expireAt
        )

        passwordResetRepository.insertCode(passwordResetParams)

        call.respond(HttpStatusCode.OK)

        call.application.launch(Dispatchers.IO) {
            resetPasswordUseCase.reset(params.email, verificationCode)
        }
    }

    post("check-code") {
        val params = call.receive<PasswordReset>()

        val passwordResetParams = passwordResetRepository.checkCode(params.code)

        if(passwordResetParams == null)
        {
            return@post call.respond(HttpStatusCode.BadRequest, "Invalid or expired code")
        }

        call.respond(HttpStatusCode.OK)
    }

    post("reset-password"){

        val updatePasswordParams = call.receive<UpdatePasswordParams>()

        val passwordResetParams = passwordResetRepository.getByCode(updatePasswordParams.reset.code)

        val hash = hashPasswordUseCase.hashPassword(updatePasswordParams.newPassword)

        userUseCase.resetPassword(passwordResetParams.userId, hash)

        passwordResetRepository.deleteByCode(updatePasswordParams.reset.code)

        call.respond(HttpStatusCode.OK)
    }
}