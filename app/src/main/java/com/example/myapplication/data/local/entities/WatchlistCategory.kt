package com.example.myapplication.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Custom watchlist categories/folders.
 * Each user can create multiple watchlist categories.
 * Default category "My Watchlist" created for new users.
 * 
 * Note: No FK constraint to User - allows guest/dev users without auth
 */
@Entity(tableName = "watchlist_categories")
data class WatchlistCategory(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val ownerId: String,
    val name: String,
    val description: String? = null,
    val createdAt: Long = System.currentTimeMillis()
)
