package tables

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.datetime

object PasswordResetToken: Table("reset_password") {
    val token: Column<String> = varchar("token", 100)
    val userId: Column<Int> = integer("user_id").references(UserTable.id)
    val expireAt = datetime("expire_at")

    override val primaryKey: PrimaryKey = PrimaryKey(token)
}