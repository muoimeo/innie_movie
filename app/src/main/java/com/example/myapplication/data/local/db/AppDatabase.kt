package com.example.myapplication.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.myapplication.data.local.dao.UserDao
import com.example.myapplication.data.local.entities.User

@Database(
    entities = [User::class],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}
