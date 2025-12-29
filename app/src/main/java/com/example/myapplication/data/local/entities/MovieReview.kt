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
@Entity
data class UserProfile(
    val name: String,
    val username: String,
    val avatarRes: Int,
    val backgroundRes: Int,
    val followersCount: Int,
    val followingCount: Int,
    val friendsCount: Int,
    val watchedCount: Int,
    val filmsThisYear: Int,
    val reviewsCount: Int,
    val favoriteFilms: List<Int>,
    val albumsCount: Int
)
@Entity
data class MovieItem(
    val title: String,
    val posterRes: Int
)