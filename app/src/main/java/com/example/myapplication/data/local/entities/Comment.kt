package com.example.myapplication.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Comments on reviews, albums, news, shots.
 * targetType: "review" | "album" | "news" | "shot"
 * 
 * Reply support: parentCommentId = null for root comments, 
 *                parentCommentId = comment.id for replies
 * 
 * Note: No FK constraint to User - allows guest/dev users without auth
 */
@Entity(tableName = "comments")
data class Comment(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val userId: String,
    val targetType: String,           // "review" | "album" | "news" | "shot"
    val targetId: Int,
    val parentCommentId: Int? = null, // null = root comment, set = reply
    val content: String,
    val createdAt: Long = System.currentTimeMillis()
)

