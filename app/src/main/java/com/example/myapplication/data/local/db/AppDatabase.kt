package com.example.myapplication.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.myapplication.data.local.dao.AlbumDao
import com.example.myapplication.data.local.dao.CommentDao
import com.example.myapplication.data.local.dao.LikeDao
import com.example.myapplication.data.local.dao.MovieDao
import com.example.myapplication.data.local.dao.NewsDao
import com.example.myapplication.data.local.dao.ReviewDao
import com.example.myapplication.data.local.dao.SavedAlbumDao
import com.example.myapplication.data.local.dao.ShotDao
import com.example.myapplication.data.local.dao.SocialDao
import com.example.myapplication.data.local.dao.UserActivityDao
import com.example.myapplication.data.local.dao.UserDao
import com.example.myapplication.data.local.dao.UserMovieStatsDao
import com.example.myapplication.data.local.dao.UserSettingsDao
import com.example.myapplication.data.local.dao.WatchlistDao
import com.example.myapplication.data.local.entities.Album
import com.example.myapplication.data.local.entities.AlbumMovie
import com.example.myapplication.data.local.entities.Comment
import com.example.myapplication.data.local.entities.Follow
import com.example.myapplication.data.local.entities.Friendship
import com.example.myapplication.data.local.entities.Like
import com.example.myapplication.data.local.entities.Movie
import com.example.myapplication.data.local.entities.News
import com.example.myapplication.data.local.entities.Review
import com.example.myapplication.data.local.entities.SavedAlbum
import com.example.myapplication.data.local.entities.Shot
import com.example.myapplication.data.local.entities.User
import com.example.myapplication.data.local.entities.UserActivity
import com.example.myapplication.data.local.entities.UserMovieStats
import com.example.myapplication.data.local.entities.UserSettings
import com.example.myapplication.data.local.entities.WatchlistCategory
import com.example.myapplication.data.local.entities.WatchlistItem

@Database(
    entities = [
        // Core entities
        User::class,
        Movie::class,
        Album::class,
        AlbumMovie::class,
        News::class,
        Shot::class,
        Review::class,
        // User data entities
        UserSettings::class,
        UserMovieStats::class,
        WatchlistCategory::class,
        WatchlistItem::class,
        Like::class,
        UserActivity::class,
        Comment::class,
        SavedAlbum::class,
        Follow::class,
        Friendship::class
    ],
    version = 28,  // Reduced synthetic stats for better diversity, fixed Profile crash
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    // Core DAOs
    abstract fun userDao(): UserDao
    abstract fun movieDao(): MovieDao
    abstract fun albumDao(): AlbumDao
    abstract fun newsDao(): NewsDao
    abstract fun shotDao(): ShotDao
    abstract fun reviewDao(): ReviewDao
    
    // User data DAOs
    abstract fun userSettingsDao(): UserSettingsDao
    abstract fun userMovieStatsDao(): UserMovieStatsDao
    abstract fun watchlistDao(): WatchlistDao
    abstract fun likeDao(): LikeDao
    abstract fun userActivityDao(): UserActivityDao
    abstract fun commentDao(): CommentDao
    abstract fun savedAlbumDao(): SavedAlbumDao
    abstract fun socialDao(): SocialDao
}
