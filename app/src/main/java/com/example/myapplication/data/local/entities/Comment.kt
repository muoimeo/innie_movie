package com.example.myapplication.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Comments on reviews and albums.
 * targetType: review | album
 * 
 * Note: No FK constraint to User - allows guest/dev users without auth
 */
@Entity(tableName = "comments")
data class Comment(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val userId: String,
    val targetType: String,  // review | album
    val targetId: Int,
    val content: String,
    val createdAt: Long = System.currentTimeMillis()
)
