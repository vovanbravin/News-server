package tables

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table

object UserAvatars: Table("user_avatars") {
    val userId: Column<Int> = integer("userId").references(UserTable.id)
    val image: Column<String> = varchar("url", 200)
}