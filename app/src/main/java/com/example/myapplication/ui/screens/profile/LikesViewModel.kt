package com.example.myapplication.ui.screens.profile

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.local.db.DatabaseProvider
import com.example.myapplication.data.local.entities.Like
import com.example.myapplication.data.local.entities.Movie
import com.example.myapplication.data.local.entities.Album
import com.example.myapplication.data.local.entities.Shot
import com.example.myapplication.data.local.entities.News
import com.example.myapplication.data.repository.LikeRepository
import com.example.myapplication.data.repository.MovieRepository
import com.example.myapplication.data.repository.AlbumRepository
import com.example.myapplication.data.repository.ShotRepository
import com.example.myapplication.data.repository.NewsRepository
import com.example.myapplication.data.session.UserSessionManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * Data class to hold liked content with metadata
 */
sealed class LikedContent {
    abstract val id: Int
    abstract val title: String
    abstract val imageUrl: String
    abstract val type: String
    
    data class LikedMovie(val movie: Movie) : LikedContent() {
        override val id = movie.id
        override val title = movie.title
        override val imageUrl = movie.posterUrl ?: ""
        override val type = "Movie"
    }
    
    data class LikedAlbum(val album: Album) : LikedContent() {
        override val id = album.id
        override val title = album.title
        override val imageUrl = album.coverUrl ?: ""
        override val type = "Album"
    }
    
    data class LikedShot(val shot: Shot) : LikedContent() {
        override val id = shot.id
        override val title = shot.caption
        override val imageUrl = shot.thumbnailUrl ?: ""
        override val type = "Shot"
    }
    
    data class LikedNews(val news: News) : LikedContent() {
        override val id = news.id
        override val title = news.title
        override val imageUrl = news.imageUrl ?: ""
        override val type = "News"
    }
}

/**
 * ViewModel for LikesScreen - fetches all liked content from database
 */
class LikesViewModel(application: Application) : AndroidViewModel(application) {
    
    private val database = DatabaseProvider.getDatabase(application)
    private val likeRepository = LikeRepository(database.likeDao())
    private val movieRepository = MovieRepository(database.movieDao())
    private val albumRepository = AlbumRepository(database.albumDao())
    private val shotRepository = ShotRepository(database.shotDao())
    private val newsRepository = NewsRepository(database.newsDao())
    
    private val currentUserId: String
        get() = UserSessionManager.getUserId()
    
    private val _allLikes = MutableStateFlow<List<LikedContent>>(emptyList())
    val allLikes: StateFlow<List<LikedContent>> = _allLikes.asStateFlow()
    
    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    // Counts by type
    private val _movieCount = MutableStateFlow(0)
    val movieCount: StateFlow<Int> = _movieCount.asStateFlow()
    
    private val _albumCount = MutableStateFlow(0)
    val albumCount: StateFlow<Int> = _albumCount.asStateFlow()
    
    private val _shotCount = MutableStateFlow(0)
    val shotCount: StateFlow<Int> = _shotCount.asStateFlow()
    
    private val _newsCount = MutableStateFlow(0)
    val newsCount: StateFlow<Int> = _newsCount.asStateFlow()
    
    init {
        loadLikedContent()
    }
    
    fun loadLikedContent() {
        viewModelScope.launch {
            _isLoading.value = true
            
            val likedItems = mutableListOf<LikedContent>()
            var movieCnt = 0
            var albumCnt = 0
            var shotCnt = 0
            var newsCnt = 0
            
            // Get all likes for user
            likeRepository.getAllLikesByUser(currentUserId).collect { likes ->
                likedItems.clear()
                movieCnt = 0
                albumCnt = 0
                shotCnt = 0
                newsCnt = 0
                
                for (like in likes) {
                    when (like.targetType) {
                        "movie" -> {
                            movieRepository.getMovieById(like.targetId)?.let { movie ->
                                likedItems.add(LikedContent.LikedMovie(movie))
                                movieCnt++
                            }
                        }
                        "album" -> {
                            albumRepository.getAlbumById(like.targetId)?.let { album ->
                                likedItems.add(LikedContent.LikedAlbum(album))
                                albumCnt++
                            }
                        }
                        "shot" -> {
                            shotRepository.getShotById(like.targetId)?.let { shot ->
                                likedItems.add(LikedContent.LikedShot(shot))
                                shotCnt++
                            }
                        }
                        "news" -> {
                            newsRepository.getNewsById(like.targetId)?.let { news ->
                                likedItems.add(LikedContent.LikedNews(news))
                                newsCnt++
                            }
                        }
                    }
                }
                
                _allLikes.value = likedItems.toList()
                _movieCount.value = movieCnt
                _albumCount.value = albumCnt
                _shotCount.value = shotCnt
                _newsCount.value = newsCnt
                _isLoading.value = false
            }
        }
    }
    
    fun getFilteredLikes(filter: String): List<LikedContent> {
        return when (filter) {
            "All" -> _allLikes.value
            "Movies" -> _allLikes.value.filterIsInstance<LikedContent.LikedMovie>()
            "Albums" -> _allLikes.value.filterIsInstance<LikedContent.LikedAlbum>()
            "Shots" -> _allLikes.value.filterIsInstance<LikedContent.LikedShot>()
            "News" -> _allLikes.value.filterIsInstance<LikedContent.LikedNews>()
            else -> _allLikes.value
        }
    }
}
