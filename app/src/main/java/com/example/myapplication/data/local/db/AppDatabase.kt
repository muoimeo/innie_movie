package com.example.myapplication.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.myapplication.data.local.dao.AlbumDao
import com.example.myapplication.data.local.dao.MovieDao
import com.example.myapplication.data.local.dao.UserDao
import com.example.myapplication.data.local.entities.Album
import com.example.myapplication.data.local.entities.AlbumMovie
import com.example.myapplication.data.local.entities.Movie
import com.example.myapplication.data.local.entities.User

@Database(
    entities = [
        User::class,
        Movie::class,
        Album::class,
        AlbumMovie::class
    ],
    version = 5,  // Incremented to force re-seed with correct Flash poster URL 
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun movieDao(): MovieDao
    abstract fun albumDao(): AlbumDao
}
