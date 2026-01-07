package com.example.myapplication.data

import android.content.Context
import android.content.SharedPreferences

/**
 * Manager for notification preferences using SharedPreferences.
 * Stores boolean preferences for each notification category.
 */
class NotificationPreferencesManager(context: Context) {
    
    private val prefs: SharedPreferences = context.getSharedPreferences(
        PREFS_NAME, 
        Context.MODE_PRIVATE
    )
    
    companion object {
        private const val PREFS_NAME = "notification_preferences"
        
        // Preference keys
        const val KEY_COMMENTS = "notify_comments"
        const val KEY_FRIEND_UPDATES = "notify_friend_updates"
        const val KEY_FRIEND_REQUESTS = "notify_friend_requests"
        const val KEY_BIRTHDAYS = "notify_birthdays"
        const val KEY_NEWS = "notify_news"
        const val KEY_ALBUMS = "notify_albums"
        const val KEY_COMMUNITY = "notify_community"
        const val KEY_MEMORIES = "notify_memories"
        const val KEY_RECOMMENDATIONS = "notify_recommendations"
        const val KEY_RELEASES = "notify_releases"
        const val KEY_REVIEWS = "notify_reviews"
        const val KEY_TRENDING = "notify_trending"
    }
    
    // Get preference (default: true - all notifications on by default)
    fun isEnabled(key: String): Boolean = prefs.getBoolean(key, true)
    
    // Set preference
    fun setEnabled(key: String, enabled: Boolean) {
        prefs.edit().putBoolean(key, enabled).apply()
    }
    
    // Get all preferences as a map
    fun getAllPreferences(): Map<String, Boolean> = mapOf(
        KEY_COMMENTS to isEnabled(KEY_COMMENTS),
        KEY_FRIEND_UPDATES to isEnabled(KEY_FRIEND_UPDATES),
        KEY_FRIEND_REQUESTS to isEnabled(KEY_FRIEND_REQUESTS),
        KEY_BIRTHDAYS to isEnabled(KEY_BIRTHDAYS),
        KEY_NEWS to isEnabled(KEY_NEWS),
        KEY_ALBUMS to isEnabled(KEY_ALBUMS),
        KEY_COMMUNITY to isEnabled(KEY_COMMUNITY),
        KEY_MEMORIES to isEnabled(KEY_MEMORIES),
        KEY_RECOMMENDATIONS to isEnabled(KEY_RECOMMENDATIONS),
        KEY_RELEASES to isEnabled(KEY_RELEASES),
        KEY_REVIEWS to isEnabled(KEY_REVIEWS),
        KEY_TRENDING to isEnabled(KEY_TRENDING)
    )
    
    // Check if a NotificationType should be shown based on preferences
    fun shouldShowNotification(type: NotificationType): Boolean {
        return when (type) {
            NotificationType.NEWS -> {
                isEnabled(KEY_NEWS) || isEnabled(KEY_RECOMMENDATIONS) || isEnabled(KEY_TRENDING)
            }
            NotificationType.COMMENT -> {
                isEnabled(KEY_COMMENTS) || isEnabled(KEY_ALBUMS) || 
                isEnabled(KEY_COMMUNITY) || isEnabled(KEY_REVIEWS)
            }
            NotificationType.TRAILER -> {
                isEnabled(KEY_RELEASES)
            }
            NotificationType.FRIEND -> {
                isEnabled(KEY_FRIEND_UPDATES) || isEnabled(KEY_FRIEND_REQUESTS) || 
                isEnabled(KEY_BIRTHDAYS) || isEnabled(KEY_MEMORIES)
            }
        }
    }
}
