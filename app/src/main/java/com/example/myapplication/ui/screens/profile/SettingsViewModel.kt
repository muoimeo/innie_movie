package com.example.myapplication.ui.screens.profile

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.local.db.DatabaseProvider
import com.example.myapplication.data.local.entities.UserSettings
import com.example.myapplication.data.repository.UserSettingsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel for SettingsScreen, managing user preferences.
 */
class SettingsViewModel(application: Application) : AndroidViewModel(application) {
    
    private val database = DatabaseProvider.getDatabase(application)
    private val userSettingsRepository = UserSettingsRepository(database.userSettingsDao())
    
    // Get userId from session manager
    private val currentUserId: String
        get() = com.example.myapplication.data.session.UserSessionManager.getUserId()
    
    private val _settings = MutableStateFlow<UserSettings?>(null)
    val settings: StateFlow<UserSettings?> = _settings.asStateFlow()
    
    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    init {
        loadSettings()
    }
    
    private fun loadSettings() {
        viewModelScope.launch {
            _isLoading.value = true
            
            // Initialize settings if not exists
            userSettingsRepository.initializeSettings(currentUserId)
            
            // Collect settings
            userSettingsRepository.getSettings(currentUserId).collect { settings ->
                _settings.value = settings
                _isLoading.value = false
            }
        }
    }
    
    fun updatePushNotifications(enabled: Boolean) {
        viewModelScope.launch {
            userSettingsRepository.updatePushNotifications(currentUserId, enabled)
        }
    }
    
    fun updateProfileVisibility(visibility: String) {
        viewModelScope.launch {
            userSettingsRepository.updateProfileVisibility(currentUserId, visibility)
        }
    }
    
    fun updateFriendRequest(allowed: String) {
        viewModelScope.launch {
            userSettingsRepository.updateFriendRequest(currentUserId, allowed)
        }
    }
}
