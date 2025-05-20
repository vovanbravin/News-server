package com.example.plugins

import Routes.userRoutes
import domain.usecase.HashPasswordUseCase
import domain.usecase.ResetPasswordUseCase
import domain.usecase.UserUseCase
import io.ktor.serialization.gson.gson
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureSerialization(userUseCase: UserUseCase, hashPasswordUseCase: HashPasswordUseCase ,resetPasswordUseCase: ResetPasswordUseCase) {
    
    install(ContentNegotiation) {
      gson()
    }

    routing {
        userRoutes(userUseCase, hashPasswordUseCase, resetPasswordUseCase)
    }
    
}
