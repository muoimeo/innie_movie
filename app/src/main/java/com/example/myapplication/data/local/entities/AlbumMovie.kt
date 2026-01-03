package com.example.myapplication.data.local.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

/**
 * Junction table for many-to-many relationship between Albums and Movies.
 * Supports ordering movies within an album via position field.
 */
@Entity(
    tableName = "album_movies",
    primaryKeys = ["albumId", "movieId"],
    foreignKeys = [
        ForeignKey(
            entity = Album::class,
            parentColumns = ["id"],
            childColumns = ["albumId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Movie::class,
            parentColumns = ["id"],
            childColumns = ["movieId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("albumId"), Index("movieId")]
)
data class AlbumMovie(
    val albumId: Int,
    val movieId: Int,
    val position: Int = 0,                // Order within album
    val addedAt: Long = System.currentTimeMillis()
)
