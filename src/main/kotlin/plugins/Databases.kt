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
import tables.PasswordResetToken
import tables.UserTable


object DatabaseFactory{

    fun configureDatabase()
    {
        Database.connect(
            "jdbc:postgresql://127.0.0.1:5432/db",
            user = "bob",
            driver = "org.postgresql.Driver"
        )

        transaction {
            SchemaUtils.create(
                UserTable,
                PasswordResetToken
            )
        }
    }


    suspend fun <T> dbQuery(block: () -> T): T{
        return withContext(Dispatchers.IO){
            transaction {
                block()
            }
        }
    }


}