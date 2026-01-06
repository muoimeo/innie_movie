package com.example.myapplication.data.local.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Shot entity - Platform-uploaded short video clips about films.
 * Linked to a related movie in the database.
 */
@Entity(
    tableName = "shots",
    foreignKeys = [
        ForeignKey(
            entity = Movie::class,
            parentColumns = ["id"],
            childColumns = ["relatedMovieId"],
            onDelete = ForeignKey.SET_NULL
        )
    ],
    indices = [Index("relatedMovieId")]
)
data class Shot(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val videoUrl: String,
    val thumbnailUrl: String?,
    val caption: String,
    val description: String?,
    val relatedMovieId: Int?,       // FK to Movie
    val likeCount: Int = 0,
    val commentCount: Int = 0,
    val shareCount: Int = 0,
    val createdAt: Long = System.currentTimeMillis()
)
