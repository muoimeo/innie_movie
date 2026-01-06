package com.example.myapplication.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * User settings for notification, privacy, and profile visibility.
 * 
 * Note: No FK constraint to User - allows guest/dev users without auth
 */
@Entity(tableName = "user_settings")
data class UserSettings(
    @PrimaryKey
    val userId: String,
    val pushNotificationsOn: Boolean = true,
    val friendRequestAllowed: String = "everyone", // everyone | friends_of_friends | no_one
    val profileVisibility: String = "public"       // public | friends | private
)
