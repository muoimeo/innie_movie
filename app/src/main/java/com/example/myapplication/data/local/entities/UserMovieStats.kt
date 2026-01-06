package com.example.myapplication.data.local.entities

import androidx.room.Entity
import androidx.room.ForeignKey

/**
 * Quick access to user's relationship with each movie.
 * Tracks: watched, favorite, rating, watchlist status.
 * 
 * Note: No FK constraint to User - allows guest/dev users without auth
 * Movie FK kept for data integrity
 */
@Entity(
    tableName = "user_movie_stats",
    primaryKeys = ["userId", "movieId"],
    foreignKeys = [
        ForeignKey(
            entity = Movie::class,
            parentColumns = ["id"],
            childColumns = ["movieId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class UserMovieStats(
    val userId: String,
    val movieId: Int,
    val isWatched: Boolean = false,
    val lastWatchedAt: Long? = null,
    val timesWatched: Int = 0,
    val rating: Float? = null,
    val isFavorite: Boolean = false,
    val inWatchlist: Boolean = false
)
