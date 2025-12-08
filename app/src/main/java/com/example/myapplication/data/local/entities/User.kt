package com.example.myapplication.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey
    val userId: String,
    val username: String,
    val email: String,
    val passwordHash: String,
    val salt: String? = null,
    val avatarUrl: String? = null,
    val bio: String? = null,
    val joinDateMillis: Long = System.currentTimeMillis()
)
