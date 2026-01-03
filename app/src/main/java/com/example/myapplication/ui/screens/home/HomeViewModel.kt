package com.example.myapplication.ui.screens.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.local.db.DatabaseProvider
import com.example.myapplication.data.local.entities.Movie
import com.example.myapplication.data.repository.AlbumRepository
import com.example.myapplication.data.repository.MovieRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel for Home screen, managing movies and albums data.
 */
class HomeViewModel(application: Application) : AndroidViewModel(application) {
    
    private val database = DatabaseProvider.getDatabase(application)
    private val movieRepository = MovieRepository(database.movieDao())
    private val albumRepository = AlbumRepository(database.albumDao())
    
    private val _movies = MutableStateFlow<List<Movie>>(emptyList())
    val movies: StateFlow<List<Movie>> = _movies.asStateFlow()
    
    private val _popularMovies = MutableStateFlow<List<Movie>>(emptyList())
    val popularMovies: StateFlow<List<Movie>> = _popularMovies.asStateFlow()
    
    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    init {
        initializeData()
    }
    
    private fun initializeData() {
        viewModelScope.launch {
            _isLoading.value = true
            
            // Seed database if empty
            movieRepository.seedDatabaseIfEmpty()
            albumRepository.seedDatabaseIfEmpty()
            
            // Collect movies from database
            movieRepository.getAllMovies().collect { movieList ->
                _movies.value = movieList
                _isLoading.value = false
            }
        }
        
        // Collect top rated movies for "Popular Films" section
        viewModelScope.launch {
            movieRepository.getTopRatedMovies(10).collect { topMovies ->
                _popularMovies.value = topMovies
            }
        }
    }
    
    fun getMovieById(id: Int, onResult: (Movie?) -> Unit) {
        viewModelScope.launch {
            val movie = movieRepository.getMovieById(id)
            onResult(movie)
        }
    }
}
