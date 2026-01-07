package com.example.myapplication.data.local.entities

import androidx.room.Embedded
import androidx.room.Relation

/**
 * Data class to join Review with Movie for UI display.
 * Used in Community feed and HomeFeed to show review with movie poster.
 */
data class ReviewWithMovie(
    @Embedded val review: Review,
    @Relation(
        parentColumn = "movieId",
        entityColumn = "id"
    )
    val movie: Movie
) {
    // Helper properties for UI
    val movieTitle: String get() = movie.title
    val movieYear: Int? get() = movie.year
    val posterUrl: String? get() = movie.posterUrl
    val rating: Float? get() = review.rating
    val reviewText: String get() = review.body
    val authorId: String get() = review.authorId
    val createdAt: Long get() = review.createdAt
}
