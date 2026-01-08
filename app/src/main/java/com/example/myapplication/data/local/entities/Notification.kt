package com.example.myapplication.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Notification entity - Real-time notifications stored in database.
 * Types: NEWS, COMMENT, TRAILER, FRIEND
 */
@Entity(tableName = "notifications")
data class Notification(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    
    val userId: String,              // Who receives this notification
    val type: String,                // "NEWS", "COMMENT", "TRAILER", "FRIEND"
    val title: String,
    val description: String,
    val imageUrl: String? = null,
    
    val relatedType: String? = null, // "news", "movie", "album", "review", "shot"
    val relatedId: Int? = null,      // ID of related content
    
    val actorUserId: String? = null, // Who triggered this (for follow/comment)
    val actorName: String? = null,   // Display name of actor
    
    val isRead: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)
