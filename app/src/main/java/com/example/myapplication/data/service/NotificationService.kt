package com.example.myapplication.data.service

import com.example.myapplication.data.local.dao.NotificationDao
import com.example.myapplication.data.local.entities.Notification

/**
 * Service for creating and managing notifications.
 * Call these methods when specific events happen in the app.
 */
class NotificationService(
    private val notificationDao: NotificationDao
) {
    /**
     * Create notification when someone comments on user's content (review, album, etc.)
     */
    suspend fun createCommentNotification(
        targetUserId: String,           // Who receives this notification
        actorUserId: String,            // Who commented
        actorName: String,              // Display name of commenter
        actorAvatarUrl: String?,        // Avatar of commenter
        contentType: String,            // "review", "album", "news", "shot"
        contentId: Int,                 // ID of the content being commented on
        contentTitle: String            // Title of the content
    ) {
        // Don't notify yourself
        if (targetUserId == actorUserId) return
        
        val notification = Notification(
            userId = targetUserId,
            type = "COMMENT",
            title = "$actorName commented on your $contentType",
            description = "\"$contentTitle\"",
            imageUrl = actorAvatarUrl,
            relatedType = contentType,
            relatedId = contentId,
            actorUserId = actorUserId,
            actorName = actorName
        )
        notificationDao.insert(notification)
    }
    
    /**
     * Create notification when someone follows the user
     */
    suspend fun createFollowNotification(
        targetUserId: String,           // Who gets followed
        followerUserId: String,         // Who is following
        followerName: String,           // Display name of follower
        followerAvatarUrl: String?      // Avatar of follower
    ) {
        // Don't notify yourself
        if (targetUserId == followerUserId) return
        
        val notification = Notification(
            userId = targetUserId,
            type = "FRIEND",
            title = "$followerName started following you",
            description = "Check out their profile!",
            imageUrl = followerAvatarUrl,
            relatedType = "user",
            relatedId = null,
            actorUserId = followerUserId,
            actorName = followerName
        )
        notificationDao.insert(notification)
    }
    
    /**
     * Create notification for new news article
     */
    suspend fun createNewsNotification(
        targetUserId: String,
        newsId: Int,
        newsTitle: String,
        newsImageUrl: String?
    ) {
        val notification = Notification(
            userId = targetUserId,
            type = "NEWS",
            title = newsTitle,
            description = "New article available!",
            imageUrl = newsImageUrl,
            relatedType = "news",
            relatedId = newsId
        )
        notificationDao.insert(notification)
    }
    
    /**
     * Create notification for new movie/trailer
     */
    suspend fun createTrailerNotification(
        targetUserId: String,
        movieId: Int,
        movieTitle: String,
        moviePosterUrl: String?
    ) {
        val notification = Notification(
            userId = targetUserId,
            type = "TRAILER",
            title = "$movieTitle - Now Available",
            description = "Check out this new release!",
            imageUrl = moviePosterUrl,
            relatedType = "movie",
            relatedId = movieId
        )
        notificationDao.insert(notification)
    }
    
    /**
     * Create notification for new album shared
     */
    suspend fun createAlbumNotification(
        targetUserId: String,
        albumId: Int,
        albumTitle: String,
        albumCoverUrl: String?,
        creatorName: String
    ) {
        val notification = Notification(
            userId = targetUserId,
            type = "FRIEND",
            title = "New Album: $albumTitle",
            description = "Created by $creatorName",
            imageUrl = albumCoverUrl,
            relatedType = "album",
            relatedId = albumId,
            actorName = creatorName
        )
        notificationDao.insert(notification)
    }
}
