package com.example.myapplication.ui.screens.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.local.db.DatabaseProvider
import com.example.myapplication.data.local.entities.Album
import com.example.myapplication.data.local.entities.Movie
import com.example.myapplication.data.local.entities.ReviewWithMovie
import com.example.myapplication.data.repository.AlbumRepository
import com.example.myapplication.data.repository.MovieRepository
import com.example.myapplication.data.repository.ReviewRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel for Home screen, managing movies, albums and reviews data.
 */
class HomeViewModel(application: Application) : AndroidViewModel(application) {
    
    companion object {
        // Set to true during development when you change sample_movies.kt URLs
        // Set to false for production or when you don't want to clear data
        const val FORCE_RESEED_FOR_DEV = false
    }
    
    private val database = DatabaseProvider.getDatabase(application)
    private val movieRepository = MovieRepository(database.movieDao())
    private val albumRepository = AlbumRepository(database.albumDao())
    private val reviewRepository = ReviewRepository(database.reviewDao())
    
    private val _movies = MutableStateFlow<List<Movie>>(emptyList())
    val movies: StateFlow<List<Movie>> = _movies.asStateFlow()
    
    private val _popularMovies = MutableStateFlow<List<Movie>>(emptyList())
    val popularMovies: StateFlow<List<Movie>> = _popularMovies.asStateFlow()
    
    // Albums from database (public albums)
    private val _popularAlbums = MutableStateFlow<List<Album>>(emptyList())
    val popularAlbums: StateFlow<List<Album>> = _popularAlbums.asStateFlow()
    
    // Popular reviews with movie data
    private val _popularReviews = MutableStateFlow<List<ReviewWithMovie>>(emptyList())
    val popularReviews: StateFlow<List<ReviewWithMovie>> = _popularReviews.asStateFlow()
    
    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    init {
        initializeData()
    }
    
    private fun initializeData() {
        viewModelScope.launch {
            _isLoading.value = true
            
            // Seed database - use forceReseed during development
            movieRepository.seedDatabaseIfEmpty(forceReseed = FORCE_RESEED_FOR_DEV)
            albumRepository.seedDatabaseIfEmpty()
            
            // Collect movies from database
            movieRepository.getAllMovies().collect { movieList ->
                _movies.value = movieList
                _isLoading.value = false
            }
        }
        
        // Collect top rated movies for "Popular Films" section
        viewModelScope.launch {
            movieRepository.getTopRatedMovies(10).collect { topMovies ->
                _popularMovies.value = topMovies
            }
        }
        
        // Collect public albums for "Popular Albums" section - sorted by likes desc
        viewModelScope.launch {
            database.albumDao().getPopularAlbumsByLikes(10).collect { albums ->
                _popularAlbums.value = albums
            }
        }
        
        // Collect recent reviews for "Popular Reviews" section
        viewModelScope.launch {
            reviewRepository.getRecentReviewsWithMovies(10).collect { reviews ->
                _popularReviews.value = reviews
            }
        }
    }
    
    fun getMovieById(id: Int, onResult: (Movie?) -> Unit) {
        viewModelScope.launch {
            val movie = movieRepository.getMovieById(id)
            onResult(movie)
        }
    }
    
    // Get album owner username
    suspend fun getAlbumOwnerName(ownerId: String): String {
        val user = database.userDao().getUserById(ownerId)
        return user?.displayName ?: user?.username ?: "Unknown"
    }
    
    // Get album stats (likes, comments)
    suspend fun getAlbumLikeCount(albumId: Int): Int {
        return database.likeDao().countLikes("album", albumId)
    }
    
    suspend fun getAlbumCommentCount(albumId: Int): Int {
        return database.commentDao().countCommentsForContent("album", albumId)
    }
    
    // Get album movies for poster display
    suspend fun getAlbumMovies(albumId: Int): List<Movie> {
        return database.albumDao().getMoviesInAlbumSync(albumId)
    }

    // Get user avatar URL
    suspend fun getUserAvatar(userId: String): String? {
        return database.userDao().getUserById(userId)?.avatarUrl
    }
}
