package com.example.myapplication.data.local.dao

import androidx.room.*
import com.example.myapplication.data.local.entities.ShowcaseMovie
import kotlinx.coroutines.flow.Flow

@Dao
interface ShowcaseDao {
    
    @Query("SELECT * FROM showcase_movies WHERE userId = :userId ORDER BY slotPosition ASC")
    fun getShowcaseMovies(userId: String): Flow<List<ShowcaseMovie>>
    
    @Query("SELECT * FROM showcase_movies WHERE userId = :userId ORDER BY slotPosition ASC")
    suspend fun getShowcaseMoviesSync(userId: String): List<ShowcaseMovie>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addToShowcase(showcase: ShowcaseMovie)
    
    @Query("DELETE FROM showcase_movies WHERE userId = :userId AND slotPosition = :slotPosition")
    suspend fun removeFromShowcase(userId: String, slotPosition: Int)
    
    @Query("DELETE FROM showcase_movies WHERE userId = :userId AND movieId = :movieId")
    suspend fun removeMovieFromShowcase(userId: String, movieId: Int)
    
    @Query("SELECT COUNT(*) FROM showcase_movies WHERE userId = :userId")
    suspend fun getShowcaseCount(userId: String): Int
    
    @Query("SELECT slotPosition FROM showcase_movies WHERE userId = :userId ORDER BY slotPosition ASC")
    suspend fun getUsedSlots(userId: String): List<Int>
    
    @Query("SELECT EXISTS(SELECT 1 FROM showcase_movies WHERE userId = :userId AND movieId = :movieId)")
    suspend fun isInShowcase(userId: String, movieId: Int): Boolean
}
