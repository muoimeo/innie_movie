package com.example.myapplication.data.local.entities

import androidx.room.Entity

/**
 * Follow relationship - one user following another.
 * Unidirectional: follower follows following.
 */
@Entity(
    tableName = "follows",
    primaryKeys = ["followerId", "followingId"]
)
data class Follow(
    val followerId: String,   // User who is following
    val followingId: String,  // User being followed
    val createdAt: Long = System.currentTimeMillis()
)
