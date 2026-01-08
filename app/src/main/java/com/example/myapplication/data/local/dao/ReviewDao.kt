package com.example.myapplication.data.local.dao

import androidx.room.*
import com.example.myapplication.data.local.entities.Review
import com.example.myapplication.data.local.entities.ReviewWithMovie
import kotlinx.coroutines.flow.Flow

@Dao
interface ReviewDao {
    
    // Get all reviews for a movie
    @Query("SELECT * FROM reviews WHERE movieId = :movieId ORDER BY createdAt DESC")
    fun getReviewsForMovie(movieId: Int): Flow<List<Review>>
    
    // Get review by ID
    @Query("SELECT * FROM reviews WHERE id = :id")
    suspend fun getById(id: Int): Review?
    
    // Get all reviews by user
    @Query("SELECT * FROM reviews WHERE authorId = :userId ORDER BY createdAt DESC")
    fun getReviewsByUser(userId: String): Flow<List<Review>>
    
    // Get user's review for a specific movie
    @Query("SELECT * FROM reviews WHERE authorId = :userId AND movieId = :movieId")
    suspend fun getUserReviewForMovie(userId: String, movieId: Int): Review?
    
    // Check if user has reviewed a movie
    @Query("SELECT EXISTS(SELECT 1 FROM reviews WHERE authorId = :userId AND movieId = :movieId)")
    suspend fun hasUserReviewed(userId: String, movieId: Int): Boolean
    
    // Insert review
    @Insert
    suspend fun insert(review: Review): Long
    
    // Update review
    @Update
    suspend fun update(review: Review)
    
    // Delete review
    @Delete
    suspend fun delete(review: Review)
    
    // Get review count for movie
    @Query("SELECT COUNT(*) FROM reviews WHERE movieId = :movieId")
    fun getReviewCount(movieId: Int): Flow<Int>
    
    // Get average rating for movie
    @Query("SELECT AVG(rating) FROM reviews WHERE movieId = :movieId AND rating IS NOT NULL")
    fun getAverageRating(movieId: Int): Flow<Float?>
    
    // Get user's review count
    @Query("SELECT COUNT(*) FROM reviews WHERE authorId = :userId")
    fun getUserReviewCount(userId: String): Flow<Int>
    
    // Get recent reviews (for community feed)
    @Query("SELECT * FROM reviews ORDER BY createdAt DESC LIMIT :limit")
    fun getRecentReviews(limit: Int): Flow<List<Review>>
        
    // Get reviews with movie info for a specific movie
    @Transaction
    @Query("SELECT * FROM reviews WHERE movieId = :movieId ORDER BY createdAt DESC")
    fun getReviewsWithMovies(movieId: Int): Flow<List<ReviewWithMovie>>
    
    // Get recent reviews with movie info (for community feed)
    @Transaction
    @Query("SELECT * FROM reviews ORDER BY createdAt DESC LIMIT :limit")
    fun getRecentReviewsWithMovies(limit: Int): Flow<List<ReviewWithMovie>>
    
    // Get reviews by user with movie info
    @Transaction
    @Query("SELECT * FROM reviews WHERE authorId = :userId ORDER BY createdAt DESC")
    fun getReviewsByUserWithMovies(userId: String): Flow<List<ReviewWithMovie>>
    
    // Get reviews by multiple authors (for Following tab - reviews from followed users)
    @Transaction
    @Query("SELECT * FROM reviews WHERE authorId IN (:authorIds) ORDER BY createdAt DESC LIMIT :limit")
    fun getReviewsByAuthorsWithMovies(authorIds: List<String>, limit: Int = 50): Flow<List<ReviewWithMovie>>
    
    // Get review IDs (sync) for seeding
    @Query("SELECT id FROM reviews")
    suspend fun getReviewIdsSync(): List<Int>
    
    // Get reviews for a specific movie (popular first)
    @Query("SELECT * FROM reviews WHERE movieId = :movieId ORDER BY createdAt DESC LIMIT :limit")
    fun getReviewsForMovie(movieId: Int, limit: Int = 10): Flow<List<Review>>

    // Get engagement-sorted reviews with movie info (For You feed)
    @Transaction
    @Query("""
        SELECT * FROM reviews 
        ORDER BY (
            (SELECT COUNT(*) FROM likes WHERE targetType = 'review' AND targetId = reviews.id) + 
            (SELECT COUNT(*) FROM comments WHERE targetType = 'review' AND targetId = reviews.id)
        ) DESC 
        LIMIT :limit
    """)
    fun getRecentReviewsByEngagementWithMovies(limit: Int): Flow<List<ReviewWithMovie>>
}
