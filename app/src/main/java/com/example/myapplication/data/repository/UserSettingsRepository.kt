package com.example.myapplication.data.repository

import com.example.myapplication.data.local.dao.UserSettingsDao
import com.example.myapplication.data.local.entities.UserSettings
import kotlinx.coroutines.flow.Flow

class UserSettingsRepository(private val dao: UserSettingsDao) {
    
    fun getSettings(userId: String): Flow<UserSettings?> = dao.getSettings(userId)
    
    suspend fun getSettingsOnce(userId: String): UserSettings? = dao.getSettingsOnce(userId)
    
    suspend fun initializeSettings(userId: String) {
        if (dao.getSettingsOnce(userId) == null) {
            dao.upsert(UserSettings(userId = userId))
        }
    }
    
    suspend fun updateSettings(settings: UserSettings) = dao.upsert(settings)
    
    suspend fun updatePushNotifications(userId: String, enabled: Boolean) = 
        dao.updatePushNotifications(userId, enabled)
    
    suspend fun updateProfileVisibility(userId: String, visibility: String) = 
        dao.updateProfileVisibility(userId, visibility)
    
    suspend fun updateFriendRequest(userId: String, allowed: String) = 
        dao.updateFriendRequest(userId, allowed)
}
