package com.example.myapplication.data.local.dao

import androidx.room.*
import com.example.myapplication.data.local.entities.News
import kotlinx.coroutines.flow.Flow

@Dao
interface NewsDao {
    
    @Query("SELECT * FROM news ORDER BY createdAt DESC")
    fun getAllNews(): Flow<List<News>>
    
    @Query("SELECT * FROM news WHERE id = :id")
    suspend fun getNewsById(id: Int): News?
    
    @Query("SELECT * FROM news WHERE title LIKE '%' || :query || '%' OR content LIKE '%' || :query || '%' ORDER BY createdAt DESC")
    fun searchNews(query: String): Flow<List<News>>
    
    @Query("SELECT COUNT(*) FROM news")
    suspend fun getCount(): Int
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(news: List<News>)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(news: News): Long
    
    @Update
    suspend fun update(news: News)
    
    @Delete
    suspend fun delete(news: News)
    
    @Query("DELETE FROM news")
    suspend fun deleteAll()
}
