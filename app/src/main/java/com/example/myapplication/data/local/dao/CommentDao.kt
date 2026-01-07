package com.example.myapplication.data.local.dao

import androidx.room.*
import com.example.myapplication.data.local.entities.Comment
import kotlinx.coroutines.flow.Flow

@Dao
interface CommentDao {
    
    // Get all comments for target (root + replies)
    @Query("SELECT * FROM comments WHERE targetType = :targetType AND targetId = :targetId ORDER BY createdAt DESC")
    fun getComments(targetType: String, targetId: Int): Flow<List<Comment>>
    
    // Get only root comments (no parent)
    @Query("SELECT * FROM comments WHERE targetType = :targetType AND targetId = :targetId AND parentCommentId IS NULL ORDER BY createdAt DESC")
    fun getRootComments(targetType: String, targetId: Int): Flow<List<Comment>>
    
    // Get replies to a comment
    @Query("SELECT * FROM comments WHERE parentCommentId = :parentId ORDER BY createdAt ASC")
    fun getReplies(parentId: Int): Flow<List<Comment>>
    
    // Get reply count for a comment
    @Query("SELECT COUNT(*) FROM comments WHERE parentCommentId = :parentId")
    fun getReplyCount(parentId: Int): Flow<Int>
    
    // Get comment by ID
    @Query("SELECT * FROM comments WHERE id = :id")
    suspend fun getById(id: Int): Comment?
    
    // Add comment
    @Insert
    suspend fun insert(comment: Comment): Long
    
    // Update comment
    @Update
    suspend fun update(comment: Comment)
    
    // Delete comment
    @Delete
    suspend fun delete(comment: Comment)
    
    // Get user's comments
    @Query("SELECT * FROM comments WHERE userId = :userId ORDER BY createdAt DESC")
    fun getByUser(userId: String): Flow<List<Comment>>
    
    // Get comment count for target (all including replies)
    @Query("SELECT COUNT(*) FROM comments WHERE targetType = :targetType AND targetId = :targetId")
    fun getCommentCount(targetType: String, targetId: Int): Flow<Int>
    
    // Get root comment count for target
    @Query("SELECT COUNT(*) FROM comments WHERE targetType = :targetType AND targetId = :targetId AND parentCommentId IS NULL")
    fun getRootCommentCount(targetType: String, targetId: Int): Flow<Int>
    
    // Count comments for content (one-time query)
    @Query("SELECT COUNT(*) FROM comments WHERE targetType = :targetType AND targetId = :targetId")
    suspend fun countCommentsForContent(targetType: String, targetId: Int): Int
}

