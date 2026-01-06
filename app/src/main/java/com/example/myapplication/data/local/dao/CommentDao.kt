package com.example.myapplication.data.local.dao

import androidx.room.*
import com.example.myapplication.data.local.entities.Comment
import kotlinx.coroutines.flow.Flow

@Dao
interface CommentDao {
    
    // Get comments for target
    @Query("SELECT * FROM comments WHERE targetType = :targetType AND targetId = :targetId ORDER BY createdAt DESC")
    fun getComments(targetType: String, targetId: Int): Flow<List<Comment>>
    
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
    
    // Get comment count for target
    @Query("SELECT COUNT(*) FROM comments WHERE targetType = :targetType AND targetId = :targetId")
    fun getCommentCount(targetType: String, targetId: Int): Flow<Int>
}
