package com.example.myapplication.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.myapplication.data.local.entities.User

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertUser(user: User)

    @Query("SELECT * FROM users WHERE username = :username LIMIT 1")
    suspend fun getUserByUsername(username: String): User?

    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    suspend fun getUserByEmail(email: String): User?

    @Query(
        """
        SELECT * FROM users 
        WHERE (username = :identifier OR email = :identifier) 
        LIMIT 1
    """
    )
    suspend fun getUserByUsernameOrEmail(identifier: String): User?

    @Query("SELECT * FROM users WHERE userId = :id LIMIT 1")
    suspend fun getUserById(id: String): User?
    
    // Update user profile fields
    @Query("""
        UPDATE users SET 
            displayName = :displayName,
            username = :username,
            email = :email,
            avatarUrl = :avatarUrl,
            coverUrl = :coverUrl,
            bio = :bio,
            gender = :gender,
            dateOfBirth = :dateOfBirth
        WHERE userId = :userId
    """)
    suspend fun updateUserProfile(
        userId: String,
        displayName: String?,
        username: String,
        email: String,
        avatarUrl: String?,
        coverUrl: String?,
        bio: String?,
        gender: String?,
        dateOfBirth: Long?
    )
}
