package com.example.myapplication.ui.screens.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.local.db.DatabaseProvider
import com.example.myapplication.data.local.entities.News
import com.example.myapplication.data.repository.LikeRepository
import com.example.myapplication.data.repository.NewsRepository
import com.example.myapplication.data.repository.UserActivityRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel for NewsFeed, managing news data with activity tracking.
 */
class NewsViewModel(application: Application) : AndroidViewModel(application) {
    
    private val database = DatabaseProvider.getDatabase(application)
    private val newsRepository = NewsRepository(database.newsDao())
    private val likeRepository = LikeRepository(database.likeDao())
    private val userActivityRepository = UserActivityRepository(database.userActivityDao())
    
    // Get userId from session manager
    private val currentUserId: String
        get() = com.example.myapplication.data.session.UserSessionManager.getUserId()
    
    private val _news = MutableStateFlow<List<News>>(emptyList())
    val news: StateFlow<List<News>> = _news.asStateFlow()
    
    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    // Liked news tracking
    private val _likedNews = MutableStateFlow<Set<Int>>(emptySet())
    val likedNews: StateFlow<Set<Int>> = _likedNews.asStateFlow()
    
    init {
        initializeData()
    }
    
    private fun initializeData() {
        viewModelScope.launch {
            _isLoading.value = true
            
            // Seed database if empty
            newsRepository.seedDatabaseIfEmpty()
            
            // Collect news from database
            newsRepository.getAllNews().collect { newsList ->
                _news.value = newsList
                _isLoading.value = false
                
                // Load liked status
                val liked = mutableSetOf<Int>()
                newsList.forEach { news ->
                    if (likeRepository.isLiked(currentUserId, "news", news.id)) {
                        liked.add(news.id)
                    }
                }
                _likedNews.value = liked
            }
        }
    }
    
    fun searchNews(query: String) {
        viewModelScope.launch {
            if (query.isBlank()) {
                newsRepository.getAllNews().collect { newsList ->
                    _news.value = newsList
                }
            } else {
                newsRepository.searchNews(query).collect { newsList ->
                    _news.value = newsList
                }
            }
        }
    }
    
    fun logNewsView(newsId: Int) {
        viewModelScope.launch {
            userActivityRepository.logNewsView(currentUserId, newsId)
        }
    }
    
    fun toggleLike(newsId: Int) {
        viewModelScope.launch {
            val newState = likeRepository.toggleLike(currentUserId, "news", newsId)
            val current = _likedNews.value.toMutableSet()
            if (newState) {
                current.add(newsId)
                userActivityRepository.logLike(currentUserId, "news", newsId)
            } else {
                current.remove(newsId)
            }
            _likedNews.value = current
        }
    }
    
    fun isLiked(newsId: Int): Boolean = _likedNews.value.contains(newsId)
}
