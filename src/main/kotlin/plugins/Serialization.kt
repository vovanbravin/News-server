package com.example.plugins

import Routes.accountRoutes
import Routes.authRoutes
import Routes.newsRoutes
import data.model.LocalDateTimeSerializer
import domain.repositories.NewsRepository
import domain.repositories.PasswordResetRepository
import domain.repositories.UserAvatarsRepository
import domain.usecase.HashPasswordUseCase
import domain.usecase.ResetPasswordUseCase
import domain.usecase.UserUseCase
import io.ktor.serialization.gson.gson
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.routing.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import java.time.LocalDateTime

fun Application.configureSerialization(
    userUseCase: UserUseCase,
    hashPasswordUseCase: HashPasswordUseCase,
    resetPasswordUseCase: ResetPasswordUseCase,
    passwordResetRepository: PasswordResetRepository,
    userAvatarsRepository: UserAvatarsRepository,
    newsRepository: NewsRepository
) {

    install(ContentNegotiation) {
        json(
            Json {
                prettyPrint = true
                encodeDefaults = true
                serializersModule = SerializersModule {
                    contextual(LocalDateTime::class, LocalDateTimeSerializer)
                }
            }
        )
    }

    routing{
        route("auth/") {
            authRoutes(userUseCase, hashPasswordUseCase, resetPasswordUseCase, passwordResetRepository)
        }

        accountRoutes(userAvatarsRepository)

        newsRoutes(newsRepository)

    }

}
