package com.example.myapplication.data.repository

import com.example.myapplication.data.allMedia
import com.example.myapplication.data.local.dao.MovieDao
import com.example.myapplication.data.local.entities.Movie
import com.example.myapplication.data.sampleMovies
import com.example.myapplication.data.sampleSeries
import kotlinx.coroutines.flow.Flow

/**
 * Repository for Movie/Series operations.
 * Handles data operations and provides clean API for ViewModels.
 */
class MovieRepository(private val movieDao: MovieDao) {
    
    // Get all movies and series
    fun getAllMovies(): Flow<List<Movie>> = movieDao.getAllMovies()
    
    // Get only films (movies)
    fun getFilmsOnly(): Flow<List<Movie>> = movieDao.getAllFilms()
    
    // Get only TV series
    fun getSeriesOnly(): Flow<List<Movie>> = movieDao.getAllSeries()
    
    // Get movie by ID
    suspend fun getMovieById(id: Int): Movie? = movieDao.getMovieById(id)
    
    fun getMovieByIdFlow(id: Int): Flow<Movie?> = movieDao.getMovieByIdFlow(id)
    
    // Search movies
    fun searchMovies(query: String): Flow<List<Movie>> = movieDao.searchMovies(query)
    
    // Get top rated
    fun getTopRatedMovies(limit: Int = 10): Flow<List<Movie>> = movieDao.getTopRatedMovies(limit)
    
    // Get by genre
    fun getMoviesByGenre(genre: String): Flow<List<Movie>> = movieDao.getMoviesByGenre(genre)
    
    // Insert movie
    suspend fun insertMovie(movie: Movie): Long = movieDao.insertMovie(movie)
    
    // Insert multiple movies
    suspend fun insertMovies(movies: List<Movie>) = movieDao.insertMovies(movies)
    
    // Update movie
    suspend fun updateMovie(movie: Movie) = movieDao.updateMovie(movie)
    
    // Delete movie
    suspend fun deleteMovie(movie: Movie) = movieDao.deleteMovie(movie)
    
    // Get count
    suspend fun getMovieCount(): Int = movieDao.getMovieCount()
    
    /**
     * Seeds the database with sample movies if empty.
     * Call this during app initialization.
     * 
     * @param forceReseed If true, clears existing data and reseeds (useful for development)
     */
    suspend fun seedDatabaseIfEmpty(forceReseed: Boolean = false) {
        if (forceReseed) {
            // Clear all movies and reseed
            movieDao.deleteAllMovies()
        }
        
        val count = movieDao.getMovieCount()
        if (count == 0) {
            // Insert all sample movies and series
            movieDao.insertMovies(allMedia)
        }
    }
}
