package com.example.myapplication.ui.screens.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.local.db.DatabaseProvider
import com.example.myapplication.data.local.entities.Album
import com.example.myapplication.data.local.entities.Movie
import com.example.myapplication.data.repository.AlbumRepository
import com.example.myapplication.data.repository.LikeRepository
import com.example.myapplication.data.repository.SavedAlbumRepository
import com.example.myapplication.data.repository.UserActivityRepository
import com.example.myapplication.data.session.UserSessionManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel for Album screens - AlbumFeed and AlbumDetailScreen.
 */
class AlbumViewModel(application: Application) : AndroidViewModel(application) {
    
    private val database = DatabaseProvider.getDatabase(application)
    private val albumRepository = AlbumRepository(database.albumDao())
    private val likeRepository = LikeRepository(database.likeDao())
    private val savedAlbumRepository = SavedAlbumRepository(database.savedAlbumDao())
    private val userActivityRepository = UserActivityRepository(database.userActivityDao())
    
    private val currentUserId: String
        get() = UserSessionManager.getUserId()
    
    private val _albums = MutableStateFlow<List<Album>>(emptyList())
    val albums: StateFlow<List<Album>> = _albums.asStateFlow()
    
    private val _selectedAlbum = MutableStateFlow<Album?>(null)
    val selectedAlbum: StateFlow<Album?> = _selectedAlbum.asStateFlow()
    
    private val _albumMovies = MutableStateFlow<List<Movie>>(emptyList())
    val albumMovies: StateFlow<List<Movie>> = _albumMovies.asStateFlow()
    
    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    // Like state for current album
    private val _isLiked = MutableStateFlow(false)
    val isLiked: StateFlow<Boolean> = _isLiked.asStateFlow()
    
    // Save state for current album (Add to Albums)
    private val _isSaved = MutableStateFlow(false)
    val isSaved: StateFlow<Boolean> = _isSaved.asStateFlow()
    
    // Album stats
    private val _likeCount = MutableStateFlow(0)
    val likeCount: StateFlow<Int> = _likeCount.asStateFlow()
    
    private val _commentCount = MutableStateFlow(0)
    val commentCount: StateFlow<Int> = _commentCount.asStateFlow()
    
    private val _viewCount = MutableStateFlow(0)
    val viewCount: StateFlow<Int> = _viewCount.asStateFlow()
    
    init {
        loadAlbums()
    }
    
    private fun loadAlbums() {
        viewModelScope.launch {
            _isLoading.value = true
            albumRepository.getPublicAlbums().collect { albumList ->
                _albums.value = albumList
                _isLoading.value = false
            }
        }
    }
    
    fun loadAlbumDetail(albumId: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            _selectedAlbum.value = albumRepository.getAlbumById(albumId)
            
            // Load movies in this album - use sync method for one-time load
            val movies = albumRepository.getMoviesInAlbumSync(albumId)
            _albumMovies.value = movies
            
            // Check if album is liked
            _isLiked.value = likeRepository.isLiked(currentUserId, "album", albumId)
            
            // Check if album is saved to library
            _isSaved.value = savedAlbumRepository.isSaved(currentUserId, albumId)
            
            // Load stats from database
            _likeCount.value = database.likeDao().countLikes("album", albumId)
            _commentCount.value = database.commentDao().countCommentsForContent("album", albumId)
            // View count - use activity log or default to 0 for new albums
            _viewCount.value = database.userActivityDao().countViews("album", albumId)
            
            // Log view for watch history
            userActivityRepository.logAlbumView(currentUserId, albumId)
            
            _isLoading.value = false
        }
    }
    
    fun toggleLike(): Boolean {
        val albumId = _selectedAlbum.value?.id ?: return false
        viewModelScope.launch {
            val newState = likeRepository.toggleLike(currentUserId, "album", albumId)
            _isLiked.value = newState
            if (newState) {
                userActivityRepository.logLike(currentUserId, "album", albumId)
            }
        }
        return !_isLiked.value // Return new state immediately for UI feedback
    }
    
    /**
     * Toggle save state for current album (Add to Albums / Remove from Albums)
     */
    fun toggleSave(): Boolean {
        val albumId = _selectedAlbum.value?.id ?: return false
        viewModelScope.launch {
            val newState = savedAlbumRepository.toggleSave(currentUserId, albumId)
            _isSaved.value = newState
        }
        return !_isSaved.value
    }
    
    fun searchAlbums(query: String) {
        viewModelScope.launch {
            if (query.isBlank()) {
                albumRepository.getPublicAlbums().collect { _albums.value = it }
            } else {
                albumRepository.searchAlbums(query).collect { _albums.value = it }
            }
        }
    }
}

