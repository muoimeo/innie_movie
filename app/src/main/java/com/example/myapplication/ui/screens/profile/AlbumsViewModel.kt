package com.example.myapplication.ui.screens.profile

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.local.db.DatabaseProvider
import com.example.myapplication.data.local.entities.Album
import com.example.myapplication.data.repository.AlbumRepository
import com.example.myapplication.data.repository.SavedAlbumRepository
import com.example.myapplication.data.session.UserSessionManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel for AlbumsScreen - manages user's albums and saved albums.
 */
class AlbumsViewModel(application: Application) : AndroidViewModel(application) {
    
    private val database = DatabaseProvider.getDatabase(application)
    private val albumRepository = AlbumRepository(database.albumDao())
    private val savedAlbumRepository = SavedAlbumRepository(database.savedAlbumDao())
    
    private val currentUserId: String
        get() = UserSessionManager.getUserId()
    
    // User's own albums
    private val _myAlbums = MutableStateFlow<List<Album>>(emptyList())
    val myAlbums: StateFlow<List<Album>> = _myAlbums.asStateFlow()
    
    // Saved albums (from other users)
    private val _savedAlbums = MutableStateFlow<List<Album>>(emptyList())
    val savedAlbums: StateFlow<List<Album>> = _savedAlbums.asStateFlow()
    
    // Loading state
    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    // Total albums count (own + saved)
    private val _totalAlbumsCount = MutableStateFlow(0)
    val totalAlbumsCount: StateFlow<Int> = _totalAlbumsCount.asStateFlow()
    
    init {
        loadAlbums()
    }
    
    private fun loadAlbums() {
        viewModelScope.launch {
            _isLoading.value = true
            
            // Load user's own albums
            albumRepository.getAlbumsByUser(currentUserId).collect { albums ->
                _myAlbums.value = albums
                updateCount()
            }
        }
        
        viewModelScope.launch {
            // Load saved albums with details
            savedAlbumRepository.getSavedAlbumsWithDetails(currentUserId).collect { albums ->
                _savedAlbums.value = albums
                updateCount()
                _isLoading.value = false
            }
        }
    }
    
    private fun updateCount() {
        _totalAlbumsCount.value = _myAlbums.value.size + _savedAlbums.value.size
    }
    
    fun removeSavedAlbum(albumId: Int) {
        viewModelScope.launch {
            savedAlbumRepository.unsave(currentUserId, albumId)
        }
    }
}
