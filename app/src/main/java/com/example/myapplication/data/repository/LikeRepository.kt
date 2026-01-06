package com.example.myapplication.data.repository

import com.example.myapplication.data.local.dao.LikeDao
import com.example.myapplication.data.local.entities.Like
import kotlinx.coroutines.flow.Flow

class LikeRepository(private val dao: LikeDao) {
    
    // Check if liked
    suspend fun isLiked(userId: String, targetType: String, targetId: Int): Boolean = 
        dao.isLiked(userId, targetType, targetId)
    
    fun isLikedFlow(userId: String, targetType: String, targetId: Int): Flow<Boolean> = 
        dao.isLikedFlow(userId, targetType, targetId)
    
    // Like/Unlike
    suspend fun like(userId: String, targetType: String, targetId: Int) {
        dao.like(Like(userId, targetType, targetId))
    }
    
    suspend fun unlike(userId: String, targetType: String, targetId: Int) {
        dao.unlike(userId, targetType, targetId)
    }
    
    suspend fun toggleLike(userId: String, targetType: String, targetId: Int): Boolean {
        return dao.toggleLike(userId, targetType, targetId)
    }
    
    // Get likes
    fun getAllLikesByUser(userId: String): Flow<List<Like>> = dao.getAllLikesByUser(userId)
    
    fun getLikesByType(userId: String, targetType: String): Flow<List<Like>> = 
        dao.getLikesByType(userId, targetType)
    
    fun getLikedMovieIds(userId: String): Flow<List<Int>> = dao.getLikedMovieIds(userId)
    
    fun getLikedAlbumIds(userId: String): Flow<List<Int>> = dao.getLikedAlbumIds(userId)
    
    // Counts
    fun getLikeCount(targetType: String, targetId: Int): Flow<Int> = 
        dao.getLikeCount(targetType, targetId)
    
    fun getUserLikeCount(userId: String): Flow<Int> = dao.getUserLikeCount(userId)
    
    // Convenience methods for specific types
    suspend fun likeMovie(userId: String, movieId: Int) = like(userId, "movie", movieId)
    suspend fun unlikeMovie(userId: String, movieId: Int) = unlike(userId, "movie", movieId)
    suspend fun toggleMovieLike(userId: String, movieId: Int) = toggleLike(userId, "movie", movieId)
    
    suspend fun likeAlbum(userId: String, albumId: Int) = like(userId, "album", albumId)
    suspend fun likeNews(userId: String, newsId: Int) = like(userId, "news", newsId)
    suspend fun likeShot(userId: String, shotId: Int) = like(userId, "shot", shotId)
    suspend fun likeReview(userId: String, reviewId: Int) = like(userId, "review", reviewId)
}
