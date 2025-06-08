package tables

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.datetime

object PasswordResetCode: Table("reset_password_code") {
    val code: Column<Int> = integer("code")
    val userId: Column<Int> = integer("user_id").references(UserTable.id)
    val expireAt = datetime("expire_at")

    override val primaryKey: PrimaryKey = PrimaryKey(code)
}