package com.example.myapplication.ui.screens.community

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.local.db.DatabaseProvider
import com.example.myapplication.data.local.entities.Movie
import com.example.myapplication.data.local.entities.WatchlistCategory
import com.example.myapplication.data.repository.LikeRepository
import com.example.myapplication.data.repository.MovieRepository
import com.example.myapplication.data.repository.UserActivityRepository
import com.example.myapplication.data.repository.UserMovieRepository
import com.example.myapplication.data.repository.WatchlistRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class MoviePageViewModel(application: Application) : AndroidViewModel(application) {
    
    private val database = DatabaseProvider.getDatabase(application)
    private val movieRepository = MovieRepository(database.movieDao())
    private val likeRepository = LikeRepository(database.likeDao())
    private val watchlistRepository = WatchlistRepository(database.watchlistDao())
    private val userMovieRepository = UserMovieRepository(database.userMovieStatsDao())
    private val userActivityRepository = UserActivityRepository(database.userActivityDao())
    
    // Get userId from session manager (supports both dev guest and real auth)
    private val currentUserId: String
        get() = com.example.myapplication.data.session.UserSessionManager.getUserId()
    
    private val _movie = MutableStateFlow<Movie?>(null)
    val movie: StateFlow<Movie?> = _movie.asStateFlow()
    
    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    // User interaction states
    private val _isLiked = MutableStateFlow(false)
    val isLiked: StateFlow<Boolean> = _isLiked.asStateFlow()
    
    private val _isInWatchlist = MutableStateFlow(false)
    val isInWatchlist: StateFlow<Boolean> = _isInWatchlist.asStateFlow()
    
    private val _isFavorite = MutableStateFlow(false)
    val isFavorite: StateFlow<Boolean> = _isFavorite.asStateFlow()
    
    private val _watchlistCategories = MutableStateFlow<List<WatchlistCategory>>(emptyList())
    val watchlistCategories: StateFlow<List<WatchlistCategory>> = _watchlistCategories.asStateFlow()
    
    // Snackbar events
    private val _snackbarEvent = MutableSharedFlow<String>()
    val snackbarEvent = _snackbarEvent.asSharedFlow()
    
    // Cache default category ID
    private var defaultCategoryId: Int? = null
    
    fun loadMovie(movieId: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            _movie.value = movieRepository.getMovieById(movieId)
            
            // Load user interaction states
            _isLiked.value = likeRepository.isLiked(currentUserId, "movie", movieId)
            
            val stats = userMovieRepository.getStatsOnce(currentUserId, movieId)
            _isInWatchlist.value = stats?.inWatchlist ?: false
            _isFavorite.value = stats?.isFavorite ?: false
            
            // Log view activity
            userActivityRepository.logMovieView(currentUserId, movieId)
            
            // Ensure default category exists
            ensureDefaultCategory()
            
            // Done loading - show content
            _isLoading.value = false
        }
        
        // Load watchlist categories separately (reactive flow)
        viewModelScope.launch {
            watchlistRepository.getCategoriesByUser(currentUserId).collect { categories ->
                _watchlistCategories.value = categories
                // Update default category ID
                if (categories.isNotEmpty()) {
                    defaultCategoryId = categories.first().id
                }
            }
        }
    }
    
    private suspend fun ensureDefaultCategory() {
        val categories = watchlistRepository.getCategoriesByUser(currentUserId).first()
        if (categories.isEmpty()) {
            val newId = watchlistRepository.createCategory(currentUserId, "My Watchlist", "Default watchlist")
            defaultCategoryId = newId.toInt()
        } else {
            defaultCategoryId = categories.first().id
        }
    }
    
    fun toggleLike() {
        val movieId = _movie.value?.id ?: return
        viewModelScope.launch {
            val newState = likeRepository.toggleMovieLike(currentUserId, movieId)
            _isLiked.value = newState
            
            if (newState) {
                userActivityRepository.logLike(currentUserId, "movie", movieId)
            }
        }
    }
    
    fun toggleWatchlist() {
        val movieId = _movie.value?.id ?: return
        val movieTitle = _movie.value?.title ?: "Movie"
        viewModelScope.launch {
            val newState = userMovieRepository.toggleWatchlist(currentUserId, movieId)
            _isInWatchlist.value = newState
            
            // Also add/remove from WatchlistItem
            val categoryId = defaultCategoryId
            if (categoryId != null) {
                if (newState) {
                    watchlistRepository.addToWatchlist(categoryId, movieId)
                    _snackbarEvent.emit("$movieTitle added to Watchlist")
                } else {
                    watchlistRepository.removeFromWatchlist(categoryId, movieId)
                    _snackbarEvent.emit("$movieTitle removed from Watchlist")
                }
            }
        }
    }
    
    fun toggleFavorite() {
        val movieId = _movie.value?.id ?: return
        viewModelScope.launch {
            val newState = userMovieRepository.toggleFavorite(currentUserId, movieId)
            _isFavorite.value = newState
        }
    }
    
    fun addToWatchlistCategory(categoryId: Int) {
        val movieId = _movie.value?.id ?: return
        viewModelScope.launch {
            watchlistRepository.addToWatchlist(categoryId, movieId)
            userMovieRepository.toggleWatchlist(currentUserId, movieId)
            _isInWatchlist.value = true
        }
    }
    
    fun markWatched() {
        val movieId = _movie.value?.id ?: return
        viewModelScope.launch {
            userMovieRepository.markWatched(currentUserId, movieId)
        }
    }
    
    fun setRating(rating: Float) {
        val movieId = _movie.value?.id ?: return
        viewModelScope.launch {
            userMovieRepository.setRating(currentUserId, movieId, rating)
        }
    }
}

