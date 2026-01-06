package com.example.myapplication.data.local.dao

import androidx.room.*
import com.example.myapplication.data.local.entities.UserActivity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserActivityDao {
    
    // Log activity
    @Insert
    suspend fun log(activity: UserActivity)
    
    // Get all activity for user
    @Query("SELECT * FROM user_activity WHERE userId = :userId ORDER BY createdAt DESC")
    fun getAllActivity(userId: String): Flow<List<UserActivity>>
    
    // Get activity by type
    @Query("SELECT * FROM user_activity WHERE userId = :userId AND actionType = :actionType ORDER BY createdAt DESC")
    fun getActivityByAction(userId: String, actionType: String): Flow<List<UserActivity>>
    
    // Get watch history (action = 'view', target = 'movie')
    @Query("SELECT * FROM user_activity WHERE userId = :userId AND actionType = 'view' AND targetType = 'movie' ORDER BY createdAt DESC")
    fun getMovieWatchHistory(userId: String): Flow<List<UserActivity>>
    
    // Get all views (for Watch History tab)
    @Query("SELECT * FROM user_activity WHERE userId = :userId AND actionType = 'view' ORDER BY createdAt DESC")
    fun getViewHistory(userId: String): Flow<List<UserActivity>>
    
    // Get recent activity (limit)
    @Query("SELECT * FROM user_activity WHERE userId = :userId ORDER BY createdAt DESC LIMIT :limit")
    fun getRecentActivity(userId: String, limit: Int): Flow<List<UserActivity>>
    
    // Check if user viewed something
    @Query("SELECT EXISTS(SELECT 1 FROM user_activity WHERE userId = :userId AND actionType = 'view' AND targetType = :targetType AND targetId = :targetId)")
    suspend fun hasViewed(userId: String, targetType: String, targetId: Int): Boolean
    
    // Get view count for content
    @Query("SELECT COUNT(*) FROM user_activity WHERE actionType = 'view' AND targetType = :targetType AND targetId = :targetId")
    fun getViewCount(targetType: String, targetId: Int): Flow<Int>
    
    // Clean old activity (optional maintenance)
    @Query("DELETE FROM user_activity WHERE createdAt < :beforeTimestamp")
    suspend fun deleteOldActivity(beforeTimestamp: Long)
}
