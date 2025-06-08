package plugins

import data.model.User
import domain.model.AuthParams
import domain.model.PasswordReset
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.requestvalidation.RequestValidation
import io.ktor.server.plugins.requestvalidation.ValidationResult

fun Application.configureRequestValidate() {
    install(RequestValidation) {

        fun validateAuthParams(authParams: AuthParams): ValidationResult{
            val emailRegex = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$")
            return if(authParams.email.isBlank() || authParams.password.isBlank())
                ValidationResult.Invalid("Fill in all the fields")
            else if (!emailRegex.matches(authParams.email))
                ValidationResult.Invalid("Invalid email format")
            else
                ValidationResult.Valid
        }

        validate<AuthParams> { authParams ->
            validateAuthParams(authParams)
        }

        validate<User>{user ->
            val authParams = AuthParams(
                user.email,
                user.password
            )
            val nicknameRegex = Regex("[@#$%^&*/.,\'\"!-]")
            validateAuthParams(authParams)
            if(user.nickname.isBlank())
                ValidationResult.Invalid("Fill in all the fields")
            else if(nicknameRegex.containsMatchIn(user.nickname))
                ValidationResult.Invalid("Invalid nickname format")
            else
                ValidationResult.Valid
        }

        validate<PasswordReset>{passwordReset ->
            val emailRegex = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$")
            if(passwordReset.email.isBlank())
                ValidationResult.Invalid("Fill in all the fields")
            else if(!emailRegex.matches(passwordReset.email))
                ValidationResult.Invalid("Invalid email format")
            else
                ValidationResult.Valid
        }

    }
}