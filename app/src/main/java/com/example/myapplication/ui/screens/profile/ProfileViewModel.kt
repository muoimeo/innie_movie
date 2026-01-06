package com.example.myapplication.ui.screens.profile

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.local.db.DatabaseProvider
import com.example.myapplication.data.local.entities.Movie
import com.example.myapplication.data.local.entities.UserActivity
import com.example.myapplication.data.local.entities.Like
import com.example.myapplication.data.repository.LikeRepository
import com.example.myapplication.data.repository.UserActivityRepository
import com.example.myapplication.data.repository.UserMovieRepository
import com.example.myapplication.data.repository.WatchlistRepository
import com.example.myapplication.data.repository.MovieRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

/**
 * ViewModel for Profile screens, providing real stats from database.
 */
class ProfileViewModel(application: Application) : AndroidViewModel(application) {
    
    private val database = DatabaseProvider.getDatabase(application)
    private val likeRepository = LikeRepository(database.likeDao())
    private val userActivityRepository = UserActivityRepository(database.userActivityDao())
    private val userMovieRepository = UserMovieRepository(database.userMovieStatsDao())
    private val watchlistRepository = WatchlistRepository(database.watchlistDao())
    private val movieRepository = MovieRepository(database.movieDao())
    
    // Get userId from session manager
    private val currentUserId: String
        get() = com.example.myapplication.data.session.UserSessionManager.getUserId()
    
    // User display info
    private val _displayName = MutableStateFlow("Guest User")
    val displayName: StateFlow<String> = _displayName.asStateFlow()
    
    private val _username = MutableStateFlow("@guest")
    val username: StateFlow<String> = _username.asStateFlow()
    
    // Social stats (default 0 for guest/dev mode)
    private val _followersCount = MutableStateFlow(0)
    val followersCount: StateFlow<Int> = _followersCount.asStateFlow()
    
    private val _friendsCount = MutableStateFlow(0)
    val friendsCount: StateFlow<Int> = _friendsCount.asStateFlow()
    
    private val _followingCount = MutableStateFlow(0)
    val followingCount: StateFlow<Int> = _followingCount.asStateFlow()
    
    // Profile stats
    private val _watchedCount = MutableStateFlow(0)
    val watchedCount: StateFlow<Int> = _watchedCount.asStateFlow()
    
    private val _likeCount = MutableStateFlow(0)
    val likeCount: StateFlow<Int> = _likeCount.asStateFlow()
    
    private val _reviewCount = MutableStateFlow(0)
    val reviewCount: StateFlow<Int> = _reviewCount.asStateFlow()
    
    // Content lists
    private val _recentActivity = MutableStateFlow<List<UserActivity>>(emptyList())
    val recentActivity: StateFlow<List<UserActivity>> = _recentActivity.asStateFlow()
    
    private val _likedMovies = MutableStateFlow<List<Movie>>(emptyList())
    val likedMovies: StateFlow<List<Movie>> = _likedMovies.asStateFlow()
    
    private val _watchlistMovies = MutableStateFlow<List<Movie>>(emptyList())
    val watchlistMovies: StateFlow<List<Movie>> = _watchlistMovies.asStateFlow()
    
    // Recent watched movies (from UserMovieStats)
    private val _recentWatchedMovies = MutableStateFlow<List<Movie>>(emptyList())
    val recentWatchedMovies: StateFlow<List<Movie>> = _recentWatchedMovies.asStateFlow()
    
    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    init {
        loadProfileData()
    }
    
    private fun loadProfileData() {
        viewModelScope.launch {
            _isLoading.value = true
            
            // Load watched count
            userMovieRepository.getWatchedCount(currentUserId).collect { count ->
                _watchedCount.value = count
            }
        }
        
        viewModelScope.launch {
            // Load like count
            likeRepository.getUserLikeCount(currentUserId).collect { count ->
                _likeCount.value = count
            }
        }
        
        viewModelScope.launch {
            // Load recent activity
            userActivityRepository.getRecentActivity(currentUserId, 20).collect { activities ->
                _recentActivity.value = activities
            }
        }
        
        viewModelScope.launch {
            // Load liked movies
            likeRepository.getLikedMovieIds(currentUserId).collect { movieIds ->
                val movies = movieIds.mapNotNull { movieRepository.getMovieById(it) }
                _likedMovies.value = movies
            }
        }
        
        viewModelScope.launch {
            // Load watchlist movies
            watchlistRepository.getAllWatchlistMovies(currentUserId).collect { movies ->
                _watchlistMovies.value = movies
            }
        }
        
        viewModelScope.launch {
            // Load recent watched movies
            userMovieRepository.getWatchedMovies(currentUserId).collect { watchedStats ->
                val movies = watchedStats.take(10).mapNotNull { stats ->
                    movieRepository.getMovieById(stats.movieId)
                }
                _recentWatchedMovies.value = movies
                _isLoading.value = false
            }
        }
    }
    
    fun refresh() {
        loadProfileData()
    }
}
