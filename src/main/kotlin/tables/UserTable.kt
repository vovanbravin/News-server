package tables

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table

object UserTable: Table("user-news") {
    val id: Column<Int> = integer("userId").autoIncrement()
    val email: Column<String> = varchar("email", 50).uniqueIndex()
    val password: Column<String> = varchar("password", 200)
    val nickname: Column<String> = varchar("nickname", 30).uniqueIndex()

    override val primaryKey: PrimaryKey = PrimaryKey(id)

}