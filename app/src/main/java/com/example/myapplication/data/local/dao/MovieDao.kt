package com.example.myapplication.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.myapplication.data.local.entities.Movie
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {
    
    // Insert operations
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovie(movie: Movie): Long
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovies(movies: List<Movie>)
    
    // Update operations
    @Update
    suspend fun updateMovie(movie: Movie)
    
    // Delete operations
    @Delete
    suspend fun deleteMovie(movie: Movie)
    
    @Query("DELETE FROM movies WHERE id = :movieId")
    suspend fun deleteMovieById(movieId: Int)
    
    // Query by ID
    @Query("SELECT * FROM movies WHERE id = :movieId")
    suspend fun getMovieById(movieId: Int): Movie?
    
    @Query("SELECT * FROM movies WHERE id = :movieId")
    fun getMovieByIdFlow(movieId: Int): Flow<Movie?>
    
    @Query("SELECT * FROM movies WHERE tmdbId = :tmdbId")
    suspend fun getMovieByTmdbId(tmdbId: String): Movie?
    
    // Get all movies/series
    @Query("SELECT * FROM movies ORDER BY createdAt DESC")
    fun getAllMovies(): Flow<List<Movie>>
    
    @Query("SELECT * FROM movies WHERE mediaType = 'movie' ORDER BY title")
    fun getAllFilms(): Flow<List<Movie>>
    
    @Query("SELECT * FROM movies WHERE mediaType = 'series' ORDER BY title")
    fun getAllSeries(): Flow<List<Movie>>
    
    // Search
    @Query("SELECT * FROM movies WHERE title LIKE '%' || :query || '%' ORDER BY title")
    fun searchMovies(query: String): Flow<List<Movie>>
    
    // Filter by genre
    @Query("SELECT * FROM movies WHERE genres LIKE '%' || :genre || '%' ORDER BY title")
    fun getMoviesByGenre(genre: String): Flow<List<Movie>>
    
    // Top rated
    @Query("SELECT * FROM movies ORDER BY rating DESC LIMIT :limit")
    fun getTopRatedMovies(limit: Int = 10): Flow<List<Movie>>
    
    // Count
    @Query("SELECT COUNT(*) FROM movies")
    suspend fun getMovieCount(): Int
    
    @Query("SELECT COUNT(*) FROM movies WHERE mediaType = :mediaType")
    suspend fun getCountByType(mediaType: String): Int
}
