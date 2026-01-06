package com.example.myapplication.data.local.entities

import androidx.room.Entity

/**
 * Universal like system for all content types.
 * targetType: movie | album | news | shot | review
 * 
 * Note: No FK constraint to User - allows guest/dev users without auth
 */
@Entity(
    tableName = "likes",
    primaryKeys = ["userId", "targetType", "targetId"]
)
data class Like(
    val userId: String,
    val targetType: String,  // movie | album | news | shot | review
    val targetId: Int,
    val createdAt: Long = System.currentTimeMillis()
)
