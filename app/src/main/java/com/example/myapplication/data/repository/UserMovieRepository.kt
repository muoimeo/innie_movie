package com.example.myapplication.data.repository

import com.example.myapplication.data.local.dao.UserMovieStatsDao
import com.example.myapplication.data.local.entities.UserMovieStats
import kotlinx.coroutines.flow.Flow

class UserMovieRepository(private val dao: UserMovieStatsDao) {
    
    fun getStats(userId: String, movieId: Int): Flow<UserMovieStats?> = dao.getStats(userId, movieId)
    
    suspend fun getStatsOnce(userId: String, movieId: Int): UserMovieStats? = dao.getStatsOnce(userId, movieId)
    
    private suspend fun ensureStats(userId: String, movieId: Int): UserMovieStats {
        return dao.getStatsOnce(userId, movieId) ?: UserMovieStats(userId, movieId).also {
            dao.upsert(it)
        }
    }
    
    // Watched
    fun getWatchedMovies(userId: String): Flow<List<UserMovieStats>> = dao.getWatchedMovies(userId)
    fun getWatchedCount(userId: String): Flow<Int> = dao.getWatchedCount(userId)
    
    suspend fun markWatched(userId: String, movieId: Int) {
        ensureStats(userId, movieId)
        dao.markWatched(userId, movieId, System.currentTimeMillis())
    }
    
    // Favorites
    fun getFavoriteMovies(userId: String): Flow<List<UserMovieStats>> = dao.getFavoriteMovies(userId)
    fun getFavoriteCount(userId: String): Flow<Int> = dao.getFavoriteCount(userId)
    
    suspend fun toggleFavorite(userId: String, movieId: Int): Boolean {
        val stats = ensureStats(userId, movieId)
        val newState = !stats.isFavorite
        dao.setFavorite(userId, movieId, newState)
        return newState
    }
    
    // Watchlist
    fun getWatchlistMovies(userId: String): Flow<List<UserMovieStats>> = dao.getWatchlistMovies(userId)
    
    suspend fun toggleWatchlist(userId: String, movieId: Int): Boolean {
        val stats = ensureStats(userId, movieId)
        val newState = !stats.inWatchlist
        dao.setInWatchlist(userId, movieId, newState)
        return newState
    }
    
    // Rating
    suspend fun setRating(userId: String, movieId: Int, rating: Float) {
        ensureStats(userId, movieId)
        dao.setRating(userId, movieId, rating)
    }
}
