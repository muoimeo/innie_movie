package com.example.myapplication.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Activity feed and watch history.
 * actionType: view | like | comment | share
 * targetType: movie | album | news | shot | review
 * 
 * Note: No FK constraint to User - allows guest/dev users without auth
 */
@Entity(tableName = "user_activity")
data class UserActivity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val userId: String,
    val actionType: String,   // view | like | comment | share
    val targetType: String,   // movie | album | news | shot | review
    val targetId: Int,
    val createdAt: Long = System.currentTimeMillis(),
    val extraData: String? = null  // JSON for additional data
)
