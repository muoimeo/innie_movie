package com.example.myapplication.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Represents a Movie or TV Series in the database.
 * Use mediaType to differentiate: "movie" or "series"
 */
@Entity(tableName = "movies")
data class Movie(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    
    val tmdbId: String? = null,          // External ID from TMDB
    val title: String,
    val year: Int? = null,
    val director: String? = null,         // Director (for movies) or Creator (for series)
    val synopsis: String? = null,         // Short tagline
    val overview: String? = null,         // Full description
    val runtimeMinutes: Int? = null,      // For movies: runtime, for series: episode runtime
    val posterUrl: String? = null,        // TMDB poster URL
    val backdropUrl: String? = null,      // TMDB backdrop URL
    val genres: String? = null,           // Comma-separated genres
    val rating: Float = 0f,               // Average rating
    val mediaType: String = "movie",      // "movie" or "series"
    val seasonCount: Int? = null,         // For series only
    val episodeCount: Int? = null,        // For series only
    val createdAt: Long = System.currentTimeMillis()
)
