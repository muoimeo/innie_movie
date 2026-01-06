package com.example.myapplication.data.local.dao

import androidx.room.*
import com.example.myapplication.data.local.entities.Like
import kotlinx.coroutines.flow.Flow

@Dao
interface LikeDao {
    
    // Check if user liked something
    @Query("SELECT EXISTS(SELECT 1 FROM likes WHERE userId = :userId AND targetType = :targetType AND targetId = :targetId)")
    suspend fun isLiked(userId: String, targetType: String, targetId: Int): Boolean
    
    @Query("SELECT EXISTS(SELECT 1 FROM likes WHERE userId = :userId AND targetType = :targetType AND targetId = :targetId)")
    fun isLikedFlow(userId: String, targetType: String, targetId: Int): Flow<Boolean>
    
    // Add like
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun like(like: Like)
    
    // Remove like
    @Query("DELETE FROM likes WHERE userId = :userId AND targetType = :targetType AND targetId = :targetId")
    suspend fun unlike(userId: String, targetType: String, targetId: Int)
    
    // Toggle like
    @Transaction
    suspend fun toggleLike(userId: String, targetType: String, targetId: Int): Boolean {
        val liked = isLiked(userId, targetType, targetId)
        if (liked) {
            unlike(userId, targetType, targetId)
        } else {
            like(Like(userId, targetType, targetId))
        }
        return !liked
    }
    
    // Get all likes by user
    @Query("SELECT * FROM likes WHERE userId = :userId ORDER BY createdAt DESC")
    fun getAllLikesByUser(userId: String): Flow<List<Like>>
    
    // Get likes by type
    @Query("SELECT * FROM likes WHERE userId = :userId AND targetType = :targetType ORDER BY createdAt DESC")
    fun getLikesByType(userId: String, targetType: String): Flow<List<Like>>
    
    // Get liked movie IDs
    @Query("SELECT targetId FROM likes WHERE userId = :userId AND targetType = 'movie'")
    fun getLikedMovieIds(userId: String): Flow<List<Int>>
    
    // Get liked album IDs
    @Query("SELECT targetId FROM likes WHERE userId = :userId AND targetType = 'album'")
    fun getLikedAlbumIds(userId: String): Flow<List<Int>>
    
    // Get like count for an item
    @Query("SELECT COUNT(*) FROM likes WHERE targetType = :targetType AND targetId = :targetId")
    fun getLikeCount(targetType: String, targetId: Int): Flow<Int>
    
    // Get user's like count
    @Query("SELECT COUNT(*) FROM likes WHERE userId = :userId")
    fun getUserLikeCount(userId: String): Flow<Int>
}
