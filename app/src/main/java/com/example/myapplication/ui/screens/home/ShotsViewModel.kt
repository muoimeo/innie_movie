package com.example.myapplication.ui.screens.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.local.db.DatabaseProvider
import com.example.myapplication.data.local.entities.Movie
import com.example.myapplication.data.local.entities.Shot
import com.example.myapplication.data.repository.ShotRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel for ShotsFeed, managing shots data.
 */
class ShotsViewModel(application: Application) : AndroidViewModel(application) {
    
    private val database = DatabaseProvider.getDatabase(application)
    private val shotRepository = ShotRepository(database.shotDao())
    
    private val _shots = MutableStateFlow<List<Shot>>(emptyList())
    val shots: StateFlow<List<Shot>> = _shots.asStateFlow()
    
    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    // Cache for related movies
    private val _relatedMovies = MutableStateFlow<Map<Int, Movie?>>(emptyMap())
    val relatedMovies: StateFlow<Map<Int, Movie?>> = _relatedMovies.asStateFlow()
    
    init {
        initializeData()
    }
    
    private fun initializeData() {
        viewModelScope.launch {
            _isLoading.value = true
            
            // Force reseed to ensure latest data with correct movie links
            shotRepository.forceReseed()
            
            // Collect shots from database
            shotRepository.getAllShots().collect { shotsList ->
                _shots.value = shotsList
                _isLoading.value = false
                
                // Pre-load related movies
                val movieMap = mutableMapOf<Int, Movie?>()
                shotsList.forEach { shot ->
                    shot.relatedMovieId?.let { movieId ->
                        if (!movieMap.containsKey(movieId)) {
                            movieMap[movieId] = shotRepository.getRelatedMovie(movieId)
                        }
                    }
                }
                _relatedMovies.value = movieMap
            }
        }
    }
    
    suspend fun getRelatedMovie(movieId: Int): Movie? {
        return shotRepository.getRelatedMovie(movieId)
    }
}
