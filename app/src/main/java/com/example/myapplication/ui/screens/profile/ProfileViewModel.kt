package com.example.myapplication.ui.screens.profile

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.local.db.DatabaseProvider
import com.example.myapplication.data.local.entities.Movie
import com.example.myapplication.data.local.entities.UserActivity
import com.example.myapplication.data.local.entities.Like
import com.example.myapplication.data.local.entities.User
import com.example.myapplication.data.local.entities.ReviewWithMovie
import com.example.myapplication.data.repository.LikeRepository
import com.example.myapplication.data.repository.UserActivityRepository
import com.example.myapplication.data.repository.UserMovieRepository
import com.example.myapplication.data.repository.WatchlistRepository
import com.example.myapplication.data.repository.MovieRepository
import com.example.myapplication.data.repository.ReviewRepository
import com.example.myapplication.data.repository.SocialRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

/**
 * ViewModel for Profile screens, providing real stats from database.
 * Supports loading data for any user (own profile or other user's profile).
 */
class ProfileViewModel(application: Application) : AndroidViewModel(application) {
    
    private val database = DatabaseProvider.getDatabase(application)
    private val userDao = database.userDao()
    private val likeRepository = LikeRepository(database.likeDao())
    private val userActivityRepository = UserActivityRepository(database.userActivityDao())
    private val userMovieRepository = UserMovieRepository(database.userMovieStatsDao())
    private val watchlistRepository = WatchlistRepository(database.watchlistDao())
    private val movieRepository = MovieRepository(database.movieDao())
    private val reviewRepository = ReviewRepository(database.reviewDao())
    private val socialRepository = SocialRepository(database.socialDao())
    private val showcaseDao = database.showcaseDao()
    
    // Get userId from session manager (for own profile)
    private val currentUserId: String
        get() = com.example.myapplication.data.session.UserSessionManager.getUserId()
    
    // Track which user's profile is currently loaded
    private var loadedUserId: String? = null
    
    // Job to cancel previous loading operations when loading new user
    private var loadingJob: Job? = null
    
    // User info (from User entity)
    private val _displayName = MutableStateFlow("Guest User")
    val displayName: StateFlow<String> = _displayName.asStateFlow()
    
    private val _username = MutableStateFlow("@guest")
    val username: StateFlow<String> = _username.asStateFlow()
    
    private val _bio = MutableStateFlow<String?>(null)
    val bio: StateFlow<String?> = _bio.asStateFlow()
    
    private val _avatarUrl = MutableStateFlow<String?>(null)
    val avatarUrl: StateFlow<String?> = _avatarUrl.asStateFlow()
    
    private val _coverUrl = MutableStateFlow<String?>(null)
    val coverUrl: StateFlow<String?> = _coverUrl.asStateFlow()
    
    // Social stats (loaded from database)
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
    
    // Album count (albums created by user)
    private val _albumCount = MutableStateFlow(0)
    val albumCount: StateFlow<Int> = _albumCount.asStateFlow()
    
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
    
    // Recent reviews by user (with movie data)
    private val _recentReviews = MutableStateFlow<List<ReviewWithMovie>>(emptyList())
    val recentReviews: StateFlow<List<ReviewWithMovie>> = _recentReviews.asStateFlow()
    
    // Showcase movies (3 slots for profile display - separate from likes)
    private val _showcaseMovies = MutableStateFlow<List<Movie>>(emptyList())
    val showcaseMovies: StateFlow<List<Movie>> = _showcaseMovies.asStateFlow()
    
    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    init {
        // Load own profile by default
        loadForUser(currentUserId)
    }
    
    /**
     * Load profile data for a specific user.
     * Call this with targetUserId when viewing another user's profile.
     * Cancels previous loading operations to prevent stale data updates.
     */
    fun loadForUser(userId: String) {
        // Cancel previous loading job to prevent multiple collectors
        loadingJob?.cancel()
        loadedUserId = userId
        
        // Start new loading job that groups all collectors
        loadingJob = viewModelScope.launch {
            _isLoading.value = true
            
            // Load user info from database (one-shot, not flow)
            val user = userDao.getUserById(userId)
            if (user != null) {
                _displayName.value = user.displayName ?: user.username
                _username.value = user.username
                _bio.value = user.bio
                _avatarUrl.value = user.avatarUrl
                _coverUrl.value = user.coverUrl
            } else {
                // Fallback for unknown user
                _displayName.value = userId.replace("user_", "").replaceFirstChar { it.uppercase() }
                _username.value = "@${userId.replace("user_", "")}"
                _bio.value = null
                _avatarUrl.value = null
                _coverUrl.value = null
            }
            
            // Launch child coroutines for reactive flows (within this job scope)
            launch {
                userMovieRepository.getWatchedCount(userId).collect { count ->
                    _watchedCount.value = count
                }
            }
            
            launch {
                likeRepository.getUserLikeCount(userId).collect { count ->
                    _likeCount.value = count
                }
            }
            
            launch {
                reviewRepository.getUserReviewCount(userId).collect { count ->
                    _reviewCount.value = count
                }
            }
            
            launch {
                database.albumDao().countAlbumsByUser(userId).collect { count ->
                    _albumCount.value = count
                }
            }
            
            launch {
                socialRepository.getFollowersCount(userId).collect { count ->
                    _followersCount.value = count
                }
            }
            
            launch {
                socialRepository.getFollowingCount(userId).collect { count ->
                    _followingCount.value = count
                }
            }
            
            launch {
                socialRepository.getFriendsCount(userId).collect { count ->
                    _friendsCount.value = count
                }
            }
            
            launch {
                userActivityRepository.getRecentActivity(userId, 20).collect { activities ->
                    _recentActivity.value = activities
                }
            }
            
            launch {
                likeRepository.getLikedMovieIds(userId).collect { movieIds ->
                    val movies = movieIds.mapNotNull { movieRepository.getMovieById(it) }
                    _likedMovies.value = movies
                }
            }
            
            launch {
                watchlistRepository.getAllWatchlistMovies(userId).collect { movies ->
                    _watchlistMovies.value = movies
                }
            }
            
            launch {
                userMovieRepository.getWatchedMovies(userId).collect { watchedStats ->
                    val movies = watchedStats.take(10).mapNotNull { stats ->
                        movieRepository.getMovieById(stats.movieId)
                    }
                    _recentWatchedMovies.value = movies
                    _isLoading.value = false
                }
            }
            
            // Load recent reviews by user (max 2 for profile display)
            launch {
                reviewRepository.getReviewsByUserWithMovies(userId).collect { reviews ->
                    _recentReviews.value = reviews.take(2)
                }
            }
            
            // Load showcase movies (3 slots)
            launch {
                showcaseDao.getShowcaseMovies(userId).collect { showcaseEntries ->
                    val movies = showcaseEntries.mapNotNull { entry ->
                        movieRepository.getMovieById(entry.movieId)
                    }
                    _showcaseMovies.value = movies
                }
            }
        }
    }
    
    fun refresh() {
        loadedUserId?.let { loadForUser(it) }
    }
    
    /**
     * Add a movie to showcase (automatically likes it too)
     */
    suspend fun addToShowcase(movieId: Int, slotPosition: Int) {
        val userId = currentUserId
        // Add to showcase
        showcaseDao.addToShowcase(
            com.example.myapplication.data.local.entities.ShowcaseMovie(
                userId = userId,
                movieId = movieId,
                slotPosition = slotPosition
            )
        )
        // Also like the movie
        likeRepository.likeMovie(userId, movieId)
    }
    
    /**
     * Remove a movie from showcase (does NOT unlike it)
     */
    suspend fun removeFromShowcase(movieId: Int) {
        showcaseDao.removeMovieFromShowcase(currentUserId, movieId)
    }
    
    /**
     * Get next available slot (0, 1, or 2)
     */
    suspend fun getNextAvailableSlot(): Int? {
        val usedSlots = showcaseDao.getUsedSlots(currentUserId)
        return (0..2).firstOrNull { it !in usedSlots }
    }
}
