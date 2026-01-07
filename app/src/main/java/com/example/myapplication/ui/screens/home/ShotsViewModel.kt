package com.example.myapplication.ui.screens.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.local.db.DatabaseProvider
import com.example.myapplication.data.local.entities.Movie
import com.example.myapplication.data.local.entities.Shot
import com.example.myapplication.data.repository.LikeRepository
import com.example.myapplication.data.repository.ShotRepository
import com.example.myapplication.data.repository.UserActivityRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel for ShotsFeed, managing shots data with activity tracking.
 */
class ShotsViewModel(application: Application) : AndroidViewModel(application) {
    
    private val database = DatabaseProvider.getDatabase(application)
    private val shotRepository = ShotRepository(database.shotDao())
    private val likeRepository = LikeRepository(database.likeDao())
    private val userActivityRepository = UserActivityRepository(database.userActivityDao())
    
    // Get userId from session manager
    private val currentUserId: String
        get() = com.example.myapplication.data.session.UserSessionManager.getUserId()
    
    private val _shots = MutableStateFlow<List<Shot>>(emptyList())
    val shots: StateFlow<List<Shot>> = _shots.asStateFlow()
    
    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    // Cache for related movies
    private val _relatedMovies = MutableStateFlow<Map<Int, Movie?>>(emptyMap())
    val relatedMovies: StateFlow<Map<Int, Movie?>> = _relatedMovies.asStateFlow()
    
    // Liked shots tracking
    private val _likedShots = MutableStateFlow<Set<Int>>(emptySet())
    val likedShots: StateFlow<Set<Int>> = _likedShots.asStateFlow()
    
    init {
        initializeData()
    }
    
    private fun initializeData() {
        viewModelScope.launch {
            _isLoading.value = true
            
            // Force reseed to ensure latest data with correct movie links
            shotRepository.forceReseed()
            
            // Collect shots from database
            shotRepository.getAllShots().collect { shotsList ->
                _shots.value = shotsList
                _isLoading.value = false
                
                // Pre-load related movies
                val movieMap = mutableMapOf<Int, Movie?>()
                shotsList.forEach { shot ->
                    shot.relatedMovieId?.let { movieId ->
                        if (!movieMap.containsKey(movieId)) {
                            movieMap[movieId] = shotRepository.getRelatedMovie(movieId)
                        }
                    }
                }
                _relatedMovies.value = movieMap
                
                // Load liked status for all shots
                val liked = mutableSetOf<Int>()
                shotsList.forEach { shot ->
                    if (likeRepository.isLiked(currentUserId, "shot", shot.id)) {
                        liked.add(shot.id)
                    }
                }
                _likedShots.value = liked
            }
        }
    }
    
    suspend fun getRelatedMovie(movieId: Int): Movie? {
        return shotRepository.getRelatedMovie(movieId)
    }
    
    fun logShotView(shotId: Int) {
        viewModelScope.launch {
            userActivityRepository.logShotView(currentUserId, shotId)
        }
    }
    
    fun toggleLike(shotId: Int) {
        viewModelScope.launch {
            val newState = likeRepository.toggleLike(currentUserId, "shot", shotId)
            val current = _likedShots.value.toMutableSet()
            if (newState) {
                current.add(shotId)
                userActivityRepository.logLike(currentUserId, "shot", shotId)
            } else {
                current.remove(shotId)
            }
            _likedShots.value = current
        }
    }
    
    fun isLiked(shotId: Int): Boolean = _likedShots.value.contains(shotId)
    
    // Refresh state for pull-to-refresh
    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()
    
    /**
     * Refresh shots data - called by pull-to-refresh
     */
    fun refresh() {
        viewModelScope.launch {
            _isRefreshing.value = true
            
            // Force reseed to get fresh data
            shotRepository.forceReseed()
            
            // Reload shots
            shotRepository.getAllShots().collect { shotsList ->
                _shots.value = shotsList
                
                // Pre-load related movies
                val movieMap = mutableMapOf<Int, Movie?>()
                shotsList.forEach { shot ->
                    shot.relatedMovieId?.let { movieId ->
                        if (!movieMap.containsKey(movieId)) {
                            movieMap[movieId] = shotRepository.getRelatedMovie(movieId)
                        }
                    }
                }
                _relatedMovies.value = movieMap
                
                // Load liked status for all shots
                val liked = mutableSetOf<Int>()
                shotsList.forEach { shot ->
                    if (likeRepository.isLiked(currentUserId, "shot", shot.id)) {
                        liked.add(shot.id)
                    }
                }
                _likedShots.value = liked
                
                _isRefreshing.value = false
            }
        }
    }
}
