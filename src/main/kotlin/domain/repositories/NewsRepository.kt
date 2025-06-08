package domain.repositories

import data.model.News

interface NewsRepository {

    suspend fun insertNews(news: News)

    suspend fun deleteNews(news: News)

    suspend fun updateNews(news: News)

    suspend fun getDifferentNews(page: Int, pageSize: Int): List<News>

}