package com.example.myapplication.data.local.dao

import androidx.room.*
import com.example.myapplication.data.local.entities.UserMovieStats
import kotlinx.coroutines.flow.Flow

@Dao
interface UserMovieStatsDao {
    
    @Query("SELECT * FROM user_movie_stats WHERE userId = :userId AND movieId = :movieId")
    fun getStats(userId: String, movieId: Int): Flow<UserMovieStats?>
    
    @Query("SELECT * FROM user_movie_stats WHERE userId = :userId AND movieId = :movieId")
    suspend fun getStatsOnce(userId: String, movieId: Int): UserMovieStats?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(stats: UserMovieStats)
    
    // Watched movies
    @Query("SELECT * FROM user_movie_stats WHERE userId = :userId AND isWatched = 1 ORDER BY lastWatchedAt DESC")
    fun getWatchedMovies(userId: String): Flow<List<UserMovieStats>>
    
    @Query("UPDATE user_movie_stats SET isWatched = 1, lastWatchedAt = :timestamp, timesWatched = timesWatched + 1 WHERE userId = :userId AND movieId = :movieId")
    suspend fun markWatched(userId: String, movieId: Int, timestamp: Long)
    
    // Favorites
    @Query("SELECT * FROM user_movie_stats WHERE userId = :userId AND isFavorite = 1")
    fun getFavoriteMovies(userId: String): Flow<List<UserMovieStats>>
    
    @Query("UPDATE user_movie_stats SET isFavorite = :isFavorite WHERE userId = :userId AND movieId = :movieId")
    suspend fun setFavorite(userId: String, movieId: Int, isFavorite: Boolean)
    
    // Watchlist  
    @Query("SELECT * FROM user_movie_stats WHERE userId = :userId AND inWatchlist = 1")
    fun getWatchlistMovies(userId: String): Flow<List<UserMovieStats>>
    
    @Query("UPDATE user_movie_stats SET inWatchlist = :inWatchlist WHERE userId = :userId AND movieId = :movieId")
    suspend fun setInWatchlist(userId: String, movieId: Int, inWatchlist: Boolean)
    
    // Rating
    @Query("UPDATE user_movie_stats SET rating = :rating WHERE userId = :userId AND movieId = :movieId")
    suspend fun setRating(userId: String, movieId: Int, rating: Float)
    
    // Counts for profile
    @Query("SELECT COUNT(*) FROM user_movie_stats WHERE userId = :userId AND isWatched = 1")
    fun getWatchedCount(userId: String): Flow<Int>
    
    @Query("SELECT COUNT(*) FROM user_movie_stats WHERE userId = :userId AND isFavorite = 1")
    fun getFavoriteCount(userId: String): Flow<Int>
}
