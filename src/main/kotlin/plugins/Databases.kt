package com.example.plugins

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.sql.Connection
import java.sql.DriverManager
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import tables.NewsImages
import tables.NewsTable
import tables.PasswordResetCode
import tables.UserAvatars
import tables.UserTable


object DatabaseFactory{

    suspend fun <T> dbQuery(block: () -> T): T{
        return withContext(Dispatchers.IO){
            transaction {
                block()
            }
        }
    }


}

fun Application.configureDatabase()
{
    val config = environment.config

    val dbUrl = config.property("postgres.url").getString()
    val dbUser = config.property("postgres.user").getString()
    val dbPass = config.property("postgres.password").getString()


    Database.connect(
        url = dbUrl,
        driver = "org.postgresql.Driver",
        user = dbUser,
        password = dbPass
    )

    transaction {
        SchemaUtils.create(
            UserTable,
            PasswordResetCode,
            UserAvatars,
            NewsTable,
            NewsImages
        )
    }
}