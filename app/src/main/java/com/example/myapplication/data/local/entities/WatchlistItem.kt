package com.example.myapplication.data.local.entities

import androidx.room.Entity
import androidx.room.ForeignKey

/**
 * Movies added to watchlist categories.
 */
@Entity(
    tableName = "watchlist_items",
    primaryKeys = ["categoryId", "movieId"],
    foreignKeys = [
        ForeignKey(
            entity = WatchlistCategory::class,
            parentColumns = ["id"],
            childColumns = ["categoryId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Movie::class,
            parentColumns = ["id"],
            childColumns = ["movieId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class WatchlistItem(
    val categoryId: Int,
    val movieId: Int,
    val addedAt: Long = System.currentTimeMillis()
)
