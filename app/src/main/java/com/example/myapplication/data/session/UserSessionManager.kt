package com.example.myapplication.data.session

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Manages user session for both dev and production modes.
 * 
 * Dev mode (SKIP_AUTH_FOR_DEV = true): Uses "guest_user" as default
 * Production mode: Uses the actual logged-in user's ID
 */
object UserSessionManager {
    
    private const val PREFS_NAME = "user_session"
    private const val KEY_USER_ID = "current_user_id"
    private const val KEY_IS_LOGGED_IN = "is_logged_in"
    
    // Default guest user for dev mode
    const val GUEST_USER_ID = "guest_user"
    
    private lateinit var prefs: SharedPreferences
    
    private val _currentUserId = MutableStateFlow(GUEST_USER_ID)
    val currentUserId: StateFlow<String> = _currentUserId.asStateFlow()
    
    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()
    
    /**
     * Initialize the session manager. Call this in Application or MainActivity.
     */
    fun init(context: Context) {
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        
        // Load saved session
        val savedUserId = prefs.getString(KEY_USER_ID, GUEST_USER_ID) ?: GUEST_USER_ID
        val savedLoggedIn = prefs.getBoolean(KEY_IS_LOGGED_IN, false)
        
        _currentUserId.value = savedUserId
        _isLoggedIn.value = savedLoggedIn
    }
    
    /**
     * Get current user ID synchronously.
     * Used by ViewModels when they don't need reactive updates.
     */
    fun getUserId(): String = _currentUserId.value
    
    /**
     * Start a dev session with guest user.
     * Called when SKIP_AUTH_FOR_DEV = true
     */
    fun startGuestSession() {
        _currentUserId.value = GUEST_USER_ID
        _isLoggedIn.value = true
        
        prefs.edit()
            .putString(KEY_USER_ID, GUEST_USER_ID)
            .putBoolean(KEY_IS_LOGGED_IN, true)
            .apply()
    }
    
    /**
     * Login with real user credentials.
     * Called after successful authentication.
     */
    fun login(userId: String) {
        _currentUserId.value = userId
        _isLoggedIn.value = true
        
        prefs.edit()
            .putString(KEY_USER_ID, userId)
            .putBoolean(KEY_IS_LOGGED_IN, true)
            .apply()
    }
    
    /**
     * Logout and clear session.
     */
    fun logout() {
        _currentUserId.value = GUEST_USER_ID
        _isLoggedIn.value = false
        
        prefs.edit()
            .remove(KEY_USER_ID)
            .putBoolean(KEY_IS_LOGGED_IN, false)
            .apply()
    }
    
    /**
     * Check if session is initialized
     */
    fun isInitialized(): Boolean = ::prefs.isInitialized
}
