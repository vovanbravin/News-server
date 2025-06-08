package data.repository

import com.example.plugins.DatabaseFactory.dbQuery
import data.model.News
import data.model.toNewsCategory
import domain.repositories.NewsRepository
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import tables.NewsImages
import tables.NewsTable

class NewsRepositoryImpl: NewsRepository {
    override suspend fun insertNews(news: News) {
        return dbQuery{
            val news_id = NewsTable
                .insert { table->
                    table[title] = news.title
                    table[description] = news.description
                    table[content] = news.content
                    table[author] = news.author
                    table[category] = news.category.toString()
                    table[time] = news.time
                } get NewsTable.newsId

            news.images.forEach { url->
                NewsImages.insert { table->
                    table[newsId] = news_id
                    table[imageUrl] = url
                }
            }
        }
    }

    override suspend fun deleteNews(news: News) {
        TODO("Not yet implemented")
    }

    override suspend fun updateNews(news: News) {
        TODO("Not yet implemented")
    }

    override suspend fun getDifferentNews(page: Int, pageSize: Int): List<News> {
        return dbQuery {
            NewsTable
                .selectAll()
                .orderBy(NewsTable.time, SortOrder.DESC)
                .limit(pageSize, offset = ((page - 1) * pageSize).toLong())
                .map { row->
                    val news = row.toNews()
                    news.images = NewsImages
                        .select { NewsImages.newsId eq news.id!!}
                        .map { it[NewsImages.imageUrl] }
                    news
                }
        }
    }
}

fun ResultRow.toNews() =
    News(
        this[NewsTable.newsId],
        this[NewsTable.category].toNewsCategory()!!,
        this[NewsTable.title],
        this[NewsTable.description],
        this[NewsTable.content],
        this[NewsTable.author],
        time = this[NewsTable.time]
    )
