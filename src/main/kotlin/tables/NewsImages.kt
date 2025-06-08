package tables


import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

object NewsImages: Table("news_images") {
    val imageId: Column<Int> = integer("id").autoIncrement()
    val newsId: Column<Int> = integer("newsId").references(NewsTable.newsId, onDelete = ReferenceOption.CASCADE)
    val imageUrl: Column<String> = varchar("imageUrl", 200)

    override val primaryKey: PrimaryKey = PrimaryKey(imageId)
}