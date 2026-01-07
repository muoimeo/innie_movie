package com.example.myapplication.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey
    val userId: String,
    val username: String,
    val displayName: String? = null,  // Full name for display
    val email: String,
    val passwordHash: String,
    val salt: String? = null,
    val avatarUrl: String? = null,
    val coverUrl: String? = null,     // Cover/banner image
    val bio: String? = null,
    val gender: String? = null,       // male | female | other | prefer_not_to_say
    val dateOfBirth: Long? = null,    // Timestamp in millis
    val joinDateMillis: Long = System.currentTimeMillis()
)
