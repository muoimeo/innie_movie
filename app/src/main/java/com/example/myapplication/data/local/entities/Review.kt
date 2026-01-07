package com.example.myapplication.data.local.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

/**
 * Movie reviews - used in community page and user profile.
 * 
 * Note: No FK constraint to User - allows guest/dev users without auth
 * Movie FK kept for data integrity
 */
@Entity(
    tableName = "reviews",
    foreignKeys = [
        ForeignKey(
            entity = Movie::class,
            parentColumns = ["id"],
            childColumns = ["movieId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Review(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val authorId: String,              // userId (String for guest support)
    val movieId: Int,
    val rating: Float? = null,         // 0-5 stars, nullable if text-only review
    val title: String? = null,         // Optional review title
    val body: String,                  // Review content
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long? = null
)
