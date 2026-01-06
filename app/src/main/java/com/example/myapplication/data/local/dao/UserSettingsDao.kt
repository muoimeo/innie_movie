package com.example.myapplication.data.local.dao

import androidx.room.*
import com.example.myapplication.data.local.entities.UserSettings
import kotlinx.coroutines.flow.Flow

@Dao
interface UserSettingsDao {
    
    @Query("SELECT * FROM user_settings WHERE userId = :userId")
    fun getSettings(userId: String): Flow<UserSettings?>
    
    @Query("SELECT * FROM user_settings WHERE userId = :userId")
    suspend fun getSettingsOnce(userId: String): UserSettings?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(settings: UserSettings)
    
    @Update
    suspend fun update(settings: UserSettings)
    
    @Query("UPDATE user_settings SET pushNotificationsOn = :enabled WHERE userId = :userId")
    suspend fun updatePushNotifications(userId: String, enabled: Boolean)
    
    @Query("UPDATE user_settings SET profileVisibility = :visibility WHERE userId = :userId")
    suspend fun updateProfileVisibility(userId: String, visibility: String)
    
    @Query("UPDATE user_settings SET friendRequestAllowed = :allowed WHERE userId = :userId")
    suspend fun updateFriendRequest(userId: String, allowed: String)
}
