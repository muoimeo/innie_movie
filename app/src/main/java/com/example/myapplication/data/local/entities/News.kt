package com.example.myapplication.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * News entity - Platform-uploaded film news articles.
 * Professional article structure like BBC/Variety.
 */
@Entity(tableName = "news")
data class News(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    
    // Article content
    val title: String,
    val subtitle: String? = null,           // Article subtitle/deck
    val content: String?,                   // Short preview/excerpt
    val body: String? = null,               // Full article body (HTML or markdown)
    
    // Author & Source
    val authorName: String? = null,         // e.g., "John Smith"
    val authorImageUrl: String? = null,     // Author avatar
    val sourceName: String,                 // e.g., "Variety", "Hollywood Reporter"
    val sourceUrl: String?,
    
    // Media
    val imageUrl: String?,                  // Hero/cover image
    val imageCaption: String? = null,       // Photo credit/caption
    
    // Categories & Tags
    val category: String? = null,           // e.g., "Film News", "Industry", "Awards"
    val tags: String? = null,               // Comma-separated tags
    
    // Engagement
    val likeCount: Int = 0,
    val viewCount: Int = 0,
    val commentCount: Int = 0,
    
    // Timestamps
    val createdAt: Long = System.currentTimeMillis(),
    val publishedAt: Long? = null,
    val readTimeMinutes: Int? = null        // Estimated reading time
)
