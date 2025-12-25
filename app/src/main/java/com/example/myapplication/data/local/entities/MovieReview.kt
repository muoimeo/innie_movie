package com.example.myapplication.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.myapplication.R

@Entity
data class MovieReview(
    val id: Int,
    val movieTitle: String,
    val year: String,
    val reviewerName: String,
    val rating: Int,
    val commentCount: Int,
    val reviewText: String,
    val posterRes: Int,
    val avatarRes: Int
)

// Dữ liệu giả lập
val forYouReviews = listOf(
    MovieReview(1, "The Subs", "2024", "The Marquee", 4, 34, "Coralie Fargeat’s second feature is an uncompromising and wildly entertaining body horror satire...", R.drawable.the_irishman, R.drawable.the_irishman),
    MovieReview(2, "Anora", "2024", "Sean Baker", 5, 120, "A breathtaking journey through the neon-lit streets...", R.drawable.the_irishman, R.drawable.the_irishman)
)
val followingReviews = listOf(
    MovieReview(1, "The Substance", "2024", "The Marquee", 4, 34, "Coralie Fargeat’s second feature is an uncompromising and wildly entertaining body horror satire...", R.drawable.the_irishman, R.drawable.the_irishman),
    MovieReview(2, "Anora", "2024", "Sean Baker", 5, 120, "A breathtaking journey through the neon-lit streets...", R.drawable.the_irishman, R.drawable.the_irishman)


)