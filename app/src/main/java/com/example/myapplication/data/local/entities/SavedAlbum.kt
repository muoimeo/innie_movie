package com.example.myapplication.data.local.entities

import androidx.room.Entity

/**
 * Saved albums - albums from other users that current user saved to their library.
 * Separate from Album entity which represents album ownership.
 */
@Entity(
    tableName = "saved_albums",
    primaryKeys = ["userId", "albumId"]
)
data class SavedAlbum(
    val userId: String,
    val albumId: Int,
    val savedAt: Long = System.currentTimeMillis()
)
