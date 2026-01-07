package com.example.myapplication.data.local.entities

import androidx.room.Entity

/**
 * Friendship - bidirectional friend relationship.
 * Status can be: pending, accepted, rejected
 */
@Entity(
    tableName = "friendships",
    primaryKeys = ["userId1", "userId2"]
)
data class Friendship(
    val userId1: String,    // User who sent request
    val userId2: String,    // User who received request
    val status: String = "pending", // pending | accepted | rejected
    val createdAt: Long = System.currentTimeMillis(),
    val acceptedAt: Long? = null
)
