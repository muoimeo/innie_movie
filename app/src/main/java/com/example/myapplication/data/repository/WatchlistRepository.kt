package com.example.myapplication.data.repository

import com.example.myapplication.data.local.dao.WatchlistDao
import com.example.myapplication.data.local.entities.Movie
import com.example.myapplication.data.local.entities.WatchlistCategory
import com.example.myapplication.data.local.entities.WatchlistItem
import kotlinx.coroutines.flow.Flow

class WatchlistRepository(private val dao: WatchlistDao) {
    
    // Categories
    fun getCategoriesByUser(userId: String): Flow<List<WatchlistCategory>> = 
        dao.getCategoriesByUser(userId)
    
    suspend fun getCategoryById(categoryId: Int): WatchlistCategory? = 
        dao.getCategoryById(categoryId)
    
    suspend fun createCategory(userId: String, name: String, description: String? = null): Long {
        return dao.insertCategory(WatchlistCategory(
            ownerId = userId,
            name = name,
            description = description
        ))
    }
    
    suspend fun updateCategory(category: WatchlistCategory) = dao.updateCategory(category)
    
    suspend fun deleteCategory(category: WatchlistCategory) = dao.deleteCategory(category)
    
    // Create default category if none exists
    suspend fun ensureDefaultCategory(userId: String): Long {
        val categories = dao.getCategoriesByUser(userId)
        // This is a simplified check - in real app, would check if list is empty
        return createCategory(userId, "My Watchlist", "Default watchlist")
    }
    
    // Items
    fun getMoviesByCategory(categoryId: Int): Flow<List<Movie>> = 
        dao.getMoviesByCategory(categoryId)
    
    fun getAllWatchlistMovies(userId: String): Flow<List<Movie>> = 
        dao.getAllWatchlistMovies(userId)
    
    suspend fun addToWatchlist(categoryId: Int, movieId: Int) {
        dao.insertItem(WatchlistItem(categoryId, movieId))
    }
    
    suspend fun removeFromWatchlist(categoryId: Int, movieId: Int) {
        dao.removeItem(categoryId, movieId)
    }
    
    suspend fun isInWatchlist(categoryId: Int, movieId: Int): Boolean = 
        dao.isInWatchlist(categoryId, movieId)
    
    fun getItemCount(categoryId: Int): Flow<Int> = dao.getItemCount(categoryId)
}
