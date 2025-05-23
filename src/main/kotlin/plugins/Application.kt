package com.example.plugins

import authentification.JwtService
import data.repository.PasswordResetRepositoryImpl
import data.repository.UserRepositoryImpl
import domain.usecase.HashPasswordUseCase
import domain.usecase.ResetPasswordUseCase
import domain.usecase.UserUseCase
import io.ktor.server.application.*
import io.ktor.server.netty.EngineMain

fun main(args: Array<String>) {
    EngineMain.main(args)
}

fun Application.module() {

    val jwtService = JwtService()
    val userRepository = UserRepositoryImpl()
    val userUseCase = UserUseCase(
        jwtService,
        userRepository
    )
    val hashPasswordUseCase = HashPasswordUseCase()
    val resetPasswordUseCase = ResetPasswordUseCase()
    val passwordResetRepository = PasswordResetRepositoryImpl()
    configureSerialization(userUseCase, hashPasswordUseCase, resetPasswordUseCase, passwordResetRepository)
    DatabaseFactory.configureDatabase()
    configureMonitoring()
    configureSecurity(userUseCase)
    configureRouting()
}
