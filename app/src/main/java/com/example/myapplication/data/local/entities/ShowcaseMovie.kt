package com.example.myapplication.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Showcase movies - the 3 "Favorite Films" displayed on profile.
 * Separate from likes - removing from showcase doesn't unlike the movie.
 */
@Entity(tableName = "showcase_movies")
data class ShowcaseMovie(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userId: String,
    val movieId: Int,
    val slotPosition: Int, // 0, 1, or 2
    val addedAt: Long = System.currentTimeMillis()
)
