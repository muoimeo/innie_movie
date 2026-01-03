package com.example.myapplication.data.local.entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Album (or List) containing movies/series.
 * Supports privacy levels: public, friends, private
 * 
 * Note: Removed ForeignKey to User for now to allow sample data seeding.
 * In production, you would add proper user creation before albums.
 */
@Entity(
    tableName = "albums",
    indices = [Index("ownerId")]
)
data class Album(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    
    val ownerId: String,                  // User who created the album
    val title: String,
    val description: String? = null,
    val coverUrl: String? = null,         // Album cover image URL
    val privacy: String = "public",       // "public", "friends", "private"
    val movieCount: Int = 0,              // Cached count for display
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)
