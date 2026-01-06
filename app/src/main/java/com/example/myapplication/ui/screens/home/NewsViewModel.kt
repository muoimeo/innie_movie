package com.example.myapplication.ui.screens.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.local.db.DatabaseProvider
import com.example.myapplication.data.local.entities.News
import com.example.myapplication.data.repository.NewsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel for NewsFeed, managing news data.
 */
class NewsViewModel(application: Application) : AndroidViewModel(application) {
    
    private val database = DatabaseProvider.getDatabase(application)
    private val newsRepository = NewsRepository(database.newsDao())
    
    private val _news = MutableStateFlow<List<News>>(emptyList())
    val news: StateFlow<List<News>> = _news.asStateFlow()
    
    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
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
}
