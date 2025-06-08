package tables

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.datetime
import java.time.LocalDateTime

object NewsTable: Table("news") {
    val newsId: Column<Int> = integer("id").autoIncrement()
    val category: Column<String> = varchar("category", 20)
    val title: Column<String> = varchar("title", 100)
    val description: Column<String> = text("description")
    val content: Column<String> = text("content")
    val author: Column<String> = varchar("author", 50)
    val time: Column<LocalDateTime> = datetime("time")

    override val primaryKey: PrimaryKey = PrimaryKey(newsId)
}