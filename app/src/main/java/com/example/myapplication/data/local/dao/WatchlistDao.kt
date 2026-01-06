package com.example.myapplication.data.local.dao

import androidx.room.*
import com.example.myapplication.data.local.entities.WatchlistCategory
import com.example.myapplication.data.local.entities.WatchlistItem
import com.example.myapplication.data.local.entities.Movie
import kotlinx.coroutines.flow.Flow

@Dao
interface WatchlistDao {
    
    // Categories
    @Query("SELECT * FROM watchlist_categories WHERE ownerId = :userId ORDER BY createdAt DESC")
    fun getCategoriesByUser(userId: String): Flow<List<WatchlistCategory>>
    
    @Query("SELECT * FROM watchlist_categories WHERE id = :categoryId")
    suspend fun getCategoryById(categoryId: Int): WatchlistCategory?
    
    @Insert
    suspend fun insertCategory(category: WatchlistCategory): Long
    
    @Update
    suspend fun updateCategory(category: WatchlistCategory)
    
    @Delete
    suspend fun deleteCategory(category: WatchlistCategory)
    
    // Items
    @Query("SELECT * FROM watchlist_items WHERE categoryId = :categoryId ORDER BY addedAt DESC")
    fun getItemsByCategory(categoryId: Int): Flow<List<WatchlistItem>>
    
    @Query("SELECT m.* FROM movies m INNER JOIN watchlist_items wi ON m.id = wi.movieId WHERE wi.categoryId = :categoryId ORDER BY wi.addedAt DESC")
    fun getMoviesByCategory(categoryId: Int): Flow<List<Movie>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(item: WatchlistItem)
    
    @Query("DELETE FROM watchlist_items WHERE categoryId = :categoryId AND movieId = :movieId")
    suspend fun removeItem(categoryId: Int, movieId: Int)
    
    @Query("SELECT EXISTS(SELECT 1 FROM watchlist_items WHERE categoryId = :categoryId AND movieId = :movieId)")
    suspend fun isInWatchlist(categoryId: Int, movieId: Int): Boolean
    
    // Count for each category
    @Query("SELECT COUNT(*) FROM watchlist_items WHERE categoryId = :categoryId")
    fun getItemCount(categoryId: Int): Flow<Int>
    
    // Get all movies in user's watchlists
    @Query("""
        SELECT DISTINCT m.* FROM movies m 
        INNER JOIN watchlist_items wi ON m.id = wi.movieId 
        INNER JOIN watchlist_categories wc ON wi.categoryId = wc.id 
        WHERE wc.ownerId = :userId 
        ORDER BY wi.addedAt DESC
    """)
    fun getAllWatchlistMovies(userId: String): Flow<List<Movie>>
}
