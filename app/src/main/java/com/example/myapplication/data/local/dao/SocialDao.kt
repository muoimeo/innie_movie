package com.example.myapplication.data.local.dao

import androidx.room.*
import com.example.myapplication.data.local.entities.Follow
import com.example.myapplication.data.local.entities.Friendship
import kotlinx.coroutines.flow.Flow

@Dao
interface SocialDao {
    
    // ====== FOLLOWS ======
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun follow(follow: Follow)
    
    @Query("DELETE FROM follows WHERE followerId = :followerId AND followingId = :followingId")
    suspend fun unfollow(followerId: String, followingId: String)
    
    @Query("SELECT EXISTS(SELECT 1 FROM follows WHERE followerId = :followerId AND followingId = :followingId)")
    suspend fun isFollowing(followerId: String, followingId: String): Boolean
    
    @Query("SELECT EXISTS(SELECT 1 FROM follows WHERE followerId = :followerId AND followingId = :followingId)")
    fun isFollowingFlow(followerId: String, followingId: String): Flow<Boolean>
    
    @Transaction
    suspend fun toggleFollow(followerId: String, followingId: String): Boolean {
        val isFollowing = isFollowing(followerId, followingId)
        if (isFollowing) {
            unfollow(followerId, followingId)
        } else {
            follow(Follow(followerId, followingId))
        }
        return !isFollowing
    }
    
    // Followers count (people following this user)
    @Query("SELECT COUNT(*) FROM follows WHERE followingId = :userId")
    fun getFollowersCount(userId: String): Flow<Int>
    
    // Following count (people this user follows)
    @Query("SELECT COUNT(*) FROM follows WHERE followerId = :userId")
    fun getFollowingCount(userId: String): Flow<Int>
    
    // Get followers list
    @Query("SELECT followerId FROM follows WHERE followingId = :userId")
    fun getFollowers(userId: String): Flow<List<String>>
    
    // Get following list
    @Query("SELECT followingId FROM follows WHERE followerId = :userId")
    fun getFollowing(userId: String): Flow<List<String>>
    
    // ====== FRIENDSHIPS ======
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun sendFriendRequest(friendship: Friendship)
    
    @Query("UPDATE friendships SET status = 'accepted', acceptedAt = :acceptedAt WHERE userId1 = :requesterId AND userId2 = :receiverId")
    suspend fun acceptFriendRequest(requesterId: String, receiverId: String, acceptedAt: Long = System.currentTimeMillis())
    
    @Query("UPDATE friendships SET status = 'rejected' WHERE userId1 = :requesterId AND userId2 = :receiverId")
    suspend fun rejectFriendRequest(requesterId: String, receiverId: String)
    
    @Query("DELETE FROM friendships WHERE (userId1 = :user1 AND userId2 = :user2) OR (userId1 = :user2 AND userId2 = :user1)")
    suspend fun unfriend(user1: String, user2: String)
    
    // Check friendship status
    @Query("""
        SELECT status FROM friendships 
        WHERE (userId1 = :user1 AND userId2 = :user2) 
           OR (userId1 = :user2 AND userId2 = :user1) 
        LIMIT 1
    """)
    suspend fun getFriendshipStatus(user1: String, user2: String): String?
    
    // Get friends count (accepted friendships)
    @Query("""
        SELECT COUNT(*) FROM friendships 
        WHERE (userId1 = :userId OR userId2 = :userId) 
          AND status = 'accepted'
    """)
    fun getFriendsCount(userId: String): Flow<Int>
    
    // Get pending friend requests received
    @Query("SELECT userId1 FROM friendships WHERE userId2 = :userId AND status = 'pending'")
    fun getPendingFriendRequests(userId: String): Flow<List<String>>
    
    // Check if friend request pending from user
    @Query("SELECT EXISTS(SELECT 1 FROM friendships WHERE userId1 = :fromUser AND userId2 = :toUser AND status = 'pending')")
    suspend fun hasPendingRequest(fromUser: String, toUser: String): Boolean
    
    // Get friends list (accepted friendships) - get the other user's ID
    @Query("""
        SELECT CASE 
            WHEN userId1 = :userId THEN userId2 
            ELSE userId1 
        END FROM friendships 
        WHERE (userId1 = :userId OR userId2 = :userId) 
          AND status = 'accepted'
    """)
    fun getFriends(userId: String): Flow<List<String>>
}
