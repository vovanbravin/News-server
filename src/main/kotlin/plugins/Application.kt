package com.example.plugins

import authentification.JwtService
import data.repository.NewsRepositoryImpl
import data.repository.PasswordResetRepositoryImpl
import data.repository.UserAvatarRepositoryImpl
import data.repository.UserRepositoryImpl
import domain.usecase.HashPasswordUseCase
import domain.usecase.ResetPasswordUseCase
import domain.usecase.UserUseCase
import io.ktor.server.application.*
import io.ktor.server.netty.*
import plugins.configureRequestValidate
import plugins.configureStatusPages

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
    val userAvatarsRepository = UserAvatarRepositoryImpl()
    val newsRepository = NewsRepositoryImpl()
    configureSecurity(userUseCase)
    configureRequestValidate()
    configureStatusPages()
    configureSerialization(userUseCase, hashPasswordUseCase, resetPasswordUseCase, passwordResetRepository, userAvatarsRepository, newsRepository)
    configureDatabase()
    configureMonitoring()
    configureRouting()
}
