package com.example.myapplication.data.repository

import com.example.myapplication.data.local.dao.NewsDao
import com.example.myapplication.data.local.entities.News
import com.example.myapplication.data.sampleNews
import kotlinx.coroutines.flow.Flow

/**
 * Repository for News operations.
 */
class NewsRepository(private val newsDao: NewsDao) {
    
    fun getAllNews(): Flow<List<News>> = newsDao.getAllNews()
    
    suspend fun getNewsById(id: Int): News? = newsDao.getNewsById(id)
    
    fun searchNews(query: String): Flow<List<News>> = newsDao.searchNews(query)
    
    suspend fun insert(news: News): Long = newsDao.insert(news)
    
    suspend fun update(news: News) = newsDao.update(news)
    
    suspend fun delete(news: News) = newsDao.delete(news)
    
    /**
     * Seeds the database with sample news if empty.
     */
    suspend fun seedDatabaseIfEmpty() {
        val count = newsDao.getCount()
        if (count == 0) {
            newsDao.insertAll(sampleNews)
        }
    }
}
