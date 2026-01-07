package com.example.myapplication.data.repository

import com.example.myapplication.data.local.dao.SocialDao
import com.example.myapplication.data.local.entities.Follow
import com.example.myapplication.data.local.entities.Friendship
import kotlinx.coroutines.flow.Flow

class SocialRepository(private val dao: SocialDao) {
    
    // ====== FOLLOWS ======
    
    suspend fun follow(followerId: String, followingId: String) {
        dao.follow(Follow(followerId, followingId))
    }
    
    suspend fun unfollow(followerId: String, followingId: String) {
        dao.unfollow(followerId, followingId)
    }
    
    suspend fun isFollowing(followerId: String, followingId: String): Boolean =
        dao.isFollowing(followerId, followingId)
    
    fun isFollowingFlow(followerId: String, followingId: String): Flow<Boolean> =
        dao.isFollowingFlow(followerId, followingId)
    
    suspend fun toggleFollow(followerId: String, followingId: String): Boolean =
        dao.toggleFollow(followerId, followingId)
    
    fun getFollowersCount(userId: String): Flow<Int> = dao.getFollowersCount(userId)
    fun getFollowingCount(userId: String): Flow<Int> = dao.getFollowingCount(userId)
    fun getFollowers(userId: String): Flow<List<String>> = dao.getFollowers(userId)
    fun getFollowing(userId: String): Flow<List<String>> = dao.getFollowing(userId)
    
    // ====== FRIENDSHIPS ======
    
    suspend fun sendFriendRequest(fromUser: String, toUser: String) {
        dao.sendFriendRequest(Friendship(fromUser, toUser))
    }
    
    suspend fun acceptFriendRequest(requesterId: String, receiverId: String) {
        dao.acceptFriendRequest(requesterId, receiverId)
    }
    
    suspend fun rejectFriendRequest(requesterId: String, receiverId: String) {
        dao.rejectFriendRequest(requesterId, receiverId)
    }
    
    suspend fun unfriend(user1: String, user2: String) {
        dao.unfriend(user1, user2)
    }
    
    suspend fun getFriendshipStatus(user1: String, user2: String): String? =
        dao.getFriendshipStatus(user1, user2)
    
    fun getFriendsCount(userId: String): Flow<Int> = dao.getFriendsCount(userId)
    fun getPendingFriendRequests(userId: String): Flow<List<String>> = dao.getPendingFriendRequests(userId)
    suspend fun hasPendingRequest(fromUser: String, toUser: String): Boolean = dao.hasPendingRequest(fromUser, toUser)
}
