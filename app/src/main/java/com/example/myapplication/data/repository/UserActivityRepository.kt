package com.example.myapplication.data.repository

import com.example.myapplication.data.local.dao.UserActivityDao
import com.example.myapplication.data.local.entities.UserActivity
import kotlinx.coroutines.flow.Flow

class UserActivityRepository(private val dao: UserActivityDao) {
    
    // Log activities
    suspend fun logView(userId: String, targetType: String, targetId: Int) {
        dao.log(UserActivity(
            userId = userId,
            actionType = "view",
            targetType = targetType,
            targetId = targetId
        ))
    }
    
    suspend fun logLike(userId: String, targetType: String, targetId: Int) {
        dao.log(UserActivity(
            userId = userId,
            actionType = "like",
            targetType = targetType,
            targetId = targetId
        ))
    }
    
    suspend fun logComment(userId: String, targetType: String, targetId: Int) {
        dao.log(UserActivity(
            userId = userId,
            actionType = "comment",
            targetType = targetType,
            targetId = targetId
        ))
    }
    
    suspend fun logShare(userId: String, targetType: String, targetId: Int, sharedTo: String? = null) {
        dao.log(UserActivity(
            userId = userId,
            actionType = "share",
            targetType = targetType,
            targetId = targetId,
            extraData = sharedTo
        ))
    }
    
    // Get activities
    fun getAllActivity(userId: String): Flow<List<UserActivity>> = dao.getAllActivity(userId)
    
    fun getViewHistory(userId: String): Flow<List<UserActivity>> = dao.getViewHistory(userId)
    
    fun getMovieWatchHistory(userId: String): Flow<List<UserActivity>> = dao.getMovieWatchHistory(userId)
    
    fun getRecentActivity(userId: String, limit: Int = 50): Flow<List<UserActivity>> = 
        dao.getRecentActivity(userId, limit)
    
    // Check viewed
    suspend fun hasViewed(userId: String, targetType: String, targetId: Int): Boolean = 
        dao.hasViewed(userId, targetType, targetId)
    
    // View counts
    fun getViewCount(targetType: String, targetId: Int): Flow<Int> = 
        dao.getViewCount(targetType, targetId)
    
    // Convenience methods
    suspend fun logMovieView(userId: String, movieId: Int) = logView(userId, "movie", movieId)
    suspend fun logShotView(userId: String, shotId: Int) = logView(userId, "shot", shotId)
    suspend fun logNewsView(userId: String, newsId: Int) = logView(userId, "news", newsId)
    suspend fun logAlbumView(userId: String, albumId: Int) = logView(userId, "album", albumId)
}
