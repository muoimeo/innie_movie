package com.example.myapplication.data.repository

import com.example.myapplication.data.local.dao.ReviewDao
import com.example.myapplication.data.local.entities.Review
import com.example.myapplication.data.local.entities.ReviewWithMovie
import kotlinx.coroutines.flow.Flow

class ReviewRepository(private val dao: ReviewDao) {
    
    // Get reviews for movie
    fun getReviewsForMovie(movieId: Int): Flow<List<Review>> = 
        dao.getReviewsForMovie(movieId)
    
    suspend fun getById(id: Int): Review? = dao.getById(id)
    
    // Get user's reviews
    fun getReviewsByUser(userId: String): Flow<List<Review>> = 
        dao.getReviewsByUser(userId)
    
    // Check if user has reviewed
    suspend fun getUserReviewForMovie(userId: String, movieId: Int): Review? = 
        dao.getUserReviewForMovie(userId, movieId)
    
    suspend fun hasUserReviewed(userId: String, movieId: Int): Boolean = 
        dao.hasUserReviewed(userId, movieId)
    
    // Create review
    suspend fun createReview(
        userId: String,
        movieId: Int,
        body: String,
        rating: Float? = null,
        title: String? = null
    ): Long {
        val review = Review(
            authorId = userId,
            movieId = movieId,
            body = body,
            rating = rating,
            title = title
        )
        return dao.insert(review)
    }
    
    // Update review
    suspend fun updateReview(review: Review) {
        val updated = review.copy(updatedAt = System.currentTimeMillis())
        dao.update(updated)
    }
    
    // Delete review
    suspend fun deleteReview(review: Review) = dao.delete(review)
    
    // Stats
    fun getReviewCount(movieId: Int): Flow<Int> = dao.getReviewCount(movieId)
    
    fun getAverageRating(movieId: Int): Flow<Float?> = dao.getAverageRating(movieId)
    
    fun getUserReviewCount(userId: String): Flow<Int> = dao.getUserReviewCount(userId)
    
    // Community feed
    fun getRecentReviews(limit: Int = 20): Flow<List<Review>> = dao.getRecentReviews(limit)
    
    // === Reviews with Movie data (for UI display) ===
    
    fun getReviewsWithMovies(movieId: Int): Flow<List<ReviewWithMovie>> = 
        dao.getReviewsWithMovies(movieId)
    
    fun getRecentReviewsWithMovies(limit: Int = 20): Flow<List<ReviewWithMovie>> = 
        dao.getRecentReviewsWithMovies(limit)
    
    fun getReviewsByUserWithMovies(userId: String): Flow<List<ReviewWithMovie>> = 
        dao.getReviewsByUserWithMovies(userId)
    
    fun getEngagementSortedReviewsWithMovies(limit: Int = 20): Flow<List<ReviewWithMovie>> = 
        dao.getRecentReviewsByEngagementWithMovies(limit)
}

